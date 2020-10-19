package trail.gcp

import trail.gcp.aggregator.TrailAggregator
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import trail.networking.model.TrailInfo
import trail.gcp.util.sendJson
import java.nio.file.Paths
import java.time.Instant
import java.util.logging.Logger
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@Serializable
data class CacheData(
  val cachedAt: Long,
  val data: List<TrailInfo> = emptyList()
)

@ExperimentalTime
class TrailAggregatorFunction : HttpFunction {

  private val logger: Logger = Logger.getLogger(TrailAggregator::class.simpleName)

  @Inject lateinit var aggregator: TrailAggregator

  init {
    gcpTrails.inject(this)
  }

  override fun service(request: HttpRequest, response: HttpResponse) = runBlocking {

    val tempDirectory = System.getProperty("java.io.tmpdir")
    val tempFile = Paths.get(tempDirectory, "trailAggregatorCache.json").toFile()

    val cache = if (tempFile.exists()) {
      logger.info("Reading from cache")
      Json.decodeFromString(tempFile.readText())
    } else {
      logger.info("No cache available")
      CacheData(0)
    }

    val timeSince = Instant.now().toEpochMilli() - cache.cachedAt
    if (timeSince <= 5.minutes.inMilliseconds) {
      logger.info("Cache still fresh, sending cache data")
      response.sendJson(cache.data, 5.minutes)
      return@runBlocking
    }

    val trails = aggregator.aggregatedTrails()

    val newCache = CacheData(
      cachedAt = Instant.now().toEpochMilli(),
      data = trails
    )

    tempFile.writeText(Json.encodeToString(newCache))
    response.sendJson(trails, 5.minutes)
  }
}
