import aggregator.TrailAggregator
import io.ktor.client.HttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import trail.networking.HtmlMorcTrailRepository
import trail.networking.MorcTrailRepository
import trail.networking.model.TrailInfo
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

private val http = HttpClient()
private val newRepository = MorcTrailRepository(http)
private val oldRepository = HtmlMorcTrailRepository(http)
private val json = Json(JsonConfiguration.Stable)

// https://us-central1-mn-trail-functions.cloudfunctions.net/trailAggregator
@ExperimentalTime
@JsName("trailAggregator")
val trailAggregator = { _: dynamic, res: dynamic ->
  GlobalScope.promise {
    val aggregator = TrailAggregator(
      morcTrailRepository = newRepository,
      htmlMorcTrailRepository = oldRepository
    )

    val trails = aggregator.aggregatedTrails()
    res.setHeader("Cache-Control", "max-age=${5.minutes.inSeconds}")
    res.setHeader("Content-Type", "application/json; charset=utf-8")
    res.send(json.stringify(TrailInfo.serializer().list, trails))
  }
}
