package trail.gcp

import cash.andrew.kotlin.common.map
import cash.andrew.kotlin.common.onFailure
import com.google.cloud.functions.BackgroundFunction
import com.google.cloud.functions.Context
import trail.networking.AggregatedTrailsRepository
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import java.util.logging.Logger
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.days

/**
 * Data required to send push notifications.
 */
@Serializable
data class NotificationTrailData(
  val name: String,
  val status: String,
  val updatedAt: Long,
  val description: String
)

suspend fun AggregatedTrailsRepository.notificationData() = getTrails(10000).map { trails ->
  trails.map { trail ->
    NotificationTrailData(
      name = trail.name,
      status = trail.status,
      updatedAt = trail.lastUpdated,
      description = trail.description
    )
  }
}

// Adds test notifications for testing.
private val DEBUG = System.getenv("STAGING")?.let { it == "true" } ?: false
private val STAGING = System.getenv("STAGING") == "true"

private val debugNotification = NotificationTrailData(
  name = "test",
  status = "open",
  updatedAt = 0,
  description = "Testing"
)

/**
 * This basically exists to make GCP happy. We currently aren't using it for anything.
 * GCP didn't like using any.
 */
data class PubSubMessage(val data: String)

/**
 * This function is hit every 5 minutes by the GCP Scheduler and
 * is what sends out notifications to phones about the trails.
 */
@ExperimentalTime
class TrailNotificationsFunction : BackgroundFunction<PubSubMessage> {

  private val logger: Logger = Logger.getLogger(TrailNotificationsFunction::class.simpleName)

  @Inject
  lateinit var aggregatedTrailsRepository: AggregatedTrailsRepository

  @Inject
  lateinit var firebaseMessaging: FirebaseMessaging

  init {
    gcpTrails.inject(this)
  }

  override fun accept(payload: PubSubMessage?, context: Context?) = runBlocking {
    val tempDirectory = System.getProperty("java.io.tmpdir")
    val tempFile = Paths.get(tempDirectory, "trailNotificationsCache.json").toFile()

    if (DEBUG) logger.info("cache file = $tempFile")

    val cache = if (tempFile.exists()) {
      logger.info("Reading from cache")
      Json.decodeFromString(tempFile.readText())
    } else {
      logger.info("No cache pulling from network")
      aggregatedTrailsRepository.notificationData().onFailure {
        logger.warning("Failed to retrieve trails $it")
        return@runBlocking
      }
        .let { if (DEBUG) it + debugNotification else it }
        .also {
          logger.info("Writing to cache")
          val dataString =
            Json.encodeToString(ListSerializer(NotificationTrailData.serializer()), it)
          tempFile.writeText(dataString)
        }
    }

    val data = aggregatedTrailsRepository.notificationData().onFailure {
      logger.warning("Failed to retrieve trails $it")
      return@runBlocking
    }.let {
      if (DEBUG) {
        it + debugNotification.copy(updatedAt = System.currentTimeMillis())
      } else it
    }

    val notifications: List<NotificationTrailData> = data.filter { trail ->
      cache.any { trail.name == it.name && trail.updatedAt != it.updatedAt }
    }.filter { it.status != "Unknown" }

    if (notifications.isEmpty()) {
      logger.info("No changes in data")
      return@runBlocking
    }

    logger.info("Writing to cache")
    val dataString = Json.encodeToString(ListSerializer(NotificationTrailData.serializer()), data)
    tempFile.writeText(dataString)

    notifications
      .filter { !STAGING || (STAGING && it.name == debugNotification.name) }
      .map {
        val topic = "v2.${it.name.replace(' ', '-')}"
        logger.info("Sending message $topic")

        Message.builder()
          .setNotification(
            Notification.builder()
              .setTitle("${it.name}: ${it.status}")
              .setBody(it.description)
              .build()
          ).setAndroidConfig(
            AndroidConfig.builder()
              .setTtl(1.days.toLongMilliseconds())
              .build()
          )
          .setTopic(topic)
          .build()
      }.forEach {
        firebaseMessaging.send(it)
      }
  }
}
