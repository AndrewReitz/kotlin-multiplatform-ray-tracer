import aggregator.TrailAggregator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import trail.networking.model.TrailInfo
import util.Response
import util.fs
import util.htmlMorcTrailRepository
import util.json
import util.morcTrailRepository
import kotlin.js.Date
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@Serializable
private data class CachData(
  val cachedAt: Long,
  val data: List<TrailInfo> = emptyList()
)

@ExperimentalTime
private fun Response.send(data: List<TrailInfo>) = this.json(
  data = data,
  serializer = TrailInfo.serializer().list,
  maxCacheAge = 5.minutes
)

private const val CACHE_DIR = "/tmp/cache.json"

// https://us-central1-mn-trail-functions.cloudfunctions.net/trailAggregator
@ExperimentalTime
@JsName("trailAggregator")
val trailAggregator = { _: dynamic, res: Response ->
  GlobalScope.promise {
    // /tmp is an in memory data storage on gcp
    val cache = if (fs.existsSync(CACHE_DIR)) {
      val data = fs.readFileSync(CACHE_DIR, "utf-8")
      json.parse(CachData.serializer(), data)
    } else CachData(0)

    val timeSince = Date.now().toLong() - cache.cachedAt
    if (timeSince <= 5.minutes.inMilliseconds) {
      res.send(cache.data)
      return@promise
    }

    val aggregator = TrailAggregator(
      morcTrailRepository = morcTrailRepository,
      htmlMorcTrailRepository = htmlMorcTrailRepository
    )
    val trails = aggregator.aggregatedTrails()

    val newCache = CachData(
      cachedAt = Date.now().toLong(),
      data = trails
    )

    fs.writeFileSync(CACHE_DIR, json.stringify(CachData.serializer(), newCache))
    res.send(trails)
  }
}
