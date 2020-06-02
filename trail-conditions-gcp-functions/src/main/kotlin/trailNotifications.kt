import trail.networking.AggregatedTrailsRepository
import cash.andrew.kotlin.common.map
import cash.andrew.kotlin.common.onFailure
import cash.andrew.kotlin.common.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import trails.config.Keys
import util.*
import kotlin.js.Date
import kotlin.time.ExperimentalTime

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

private const val CACHE_FILE = "/tmp/notificationCache.json"

suspend fun AggregatedTrailsRepository.notificationData() = getTrails().map { trails ->
  trails.map { trail ->
    NotificationTrailData(
      name = trail.name,
      status = trail.status,
      updatedAt = trail.lastUpdated,
      description = trail.description
    )
  }
}

fun List<NotificationTrailData>.cache() {
  val dataString = json.stringify(NotificationTrailData.serializer().list, this)
  fs.writeFileSync(CACHE_FILE, dataString)
}

// todo cleanup once intellij is better at kotlin-js
/**
 * This function is hit every 5 minutes by the GCP Scheduler and
 * is what sends out notifications to phones about the trails.
 */
@ExperimentalTime
val trailNotifications = { _: dynamic, _: dynamic ->
  GlobalScope.promise {
    val cache = if (fs.existsSync(CACHE_FILE)) {
      val data = fs.readFileSync(CACHE_FILE, "utf-8")
      json.parse(NotificationTrailData.serializer().list, data)
    } else {
      aggregatedTrailsRepository.notificationData().onFailure {
        println("Failed to retrieve trails $it")
        return@promise
      }.also { it.cache() }
    } + NotificationTrailData("test", "Open", 0, "testing")

    val data = aggregatedTrailsRepository.notificationData().onFailure {
      println("Failed to retrieve trails $it")
      return@promise
    } + NotificationTrailData("test", "Open", Date.now().toLong(), "testing")

    val notifications: List<NotificationTrailData> = data.map { trail ->
        cache.find { trail.name == it.name && trail.updatedAt != it.updatedAt }
      }
      .filterNotNull()

    if (notifications.isEmpty()) {
      println("No changes in data")
      return@promise
    }

    data.cache()

    val serviceAccount = ServiceAccount(
      clientEmail = Keys.firebase_client_email,
      privateKey = Keys.firebase_private_key,
      projectId = Keys.firebase_project_id
    )

    if (admin.apps?.isEmpty() == true) {
      val creds = admin.credential.cert(serviceAccount)
      admin.initializeApp(Credentials(creds))
    }

    notifications.map {
      Message(
        notification = Message.Notification(
          title = "${it.name}: ${it.status}",
          body = it.description
        ),
        android = Message.Android(
          // 1.days.toLongMilliseconds() returns a negative number...
          ttl = 3600 * 1000 * 24
        ),
        topic = "v2.${it.name.replace(' ', '-')}"
      )
    }.forEach {
      println("Sending message ${it.topic}")
      admin.messaging().send(it).then { response: dynamic ->
        println("Successfully sent message: $response")
      }.catch { error -> println("Error sending message $error") }
    }
  }
}
