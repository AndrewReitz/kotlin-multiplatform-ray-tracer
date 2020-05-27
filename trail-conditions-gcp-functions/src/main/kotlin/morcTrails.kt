import cash.andrew.kotlin.common.Failure
import cash.andrew.kotlin.common.Success
import io.ktor.client.HttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.serialization.builtins.list
import trail.networking.MorcTrailRepository
import trail.networking.model.MorcTrail
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.minutes

private val http = HttpClient()
private val repository = MorcTrailRepository(http)

// https://us-central1-mn-trail-functions.cloudfunctions.net/morcTrails
@ExperimentalTime
@JsName("morcTrails")
val morcTrails = { _: dynamic, res: Response ->
  morcTrailsIncomplete(res)
}

@ExperimentalTime
private fun morcTrailsIncomplete(res: Response) {
  res.setHeader("Cache-Control", "max-age=${1.days.inSeconds}")
  res.status(501)
  res.send("Not implemented yet")
}

@ExperimentalTime
private fun morcTrailsComplete(res: Response) {
  GlobalScope.promise {
    when(val result = repository.getTrails()) {
      is Success -> {
        res.json(
          data = result.value,
          serializer = MorcTrail.serializer().list,
          maxCacheAge = 5.minutes
        )
      }
      is Failure -> {
        console.log("Error getting data from backend ${result.reason}")
        res.status(500)
        res.send("There was an error loading data from upstream server")
      }
    }
  }
}
