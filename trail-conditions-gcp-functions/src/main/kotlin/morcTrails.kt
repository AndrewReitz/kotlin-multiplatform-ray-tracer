import cash.andrew.kotlin.common.Failure
import cash.andrew.kotlin.common.Success
import io.ktor.client.HttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import trail.networking.MorcTrailRepository
import trail.networking.model.MorcTrail

private val http = HttpClient()
private val repository = MorcTrailRepository(http)
private val json = Json(JsonConfiguration.Stable)

@JsName("morcTrails")
val morcTrails = { _: dynamic, res: dynamic ->
  morcTrailsIncomplete(res)
}

private fun morcTrailsIncomplete(res: dynamic) {
  res.setHeader("Cache-Control", "max-age=3600")
  res.status(501)
  res.send("Not implemented yet")
}

private fun morcTrailsComplete(res: dynamic) {
  GlobalScope.promise {
    when(val result = repository.getTrails()) {
      is Success -> {
        res.setHeader("Cache-Control", "max-age=300")
        res.setHeader("Content-Type", "application/json; charset=utf-8")
        res.send(json.stringify(MorcTrail.serializer().list, result.value))
      }
      is Failure -> {
        console.log("Error getting data from backend ${result.reason}")
        res.status(500)
        res.send("There was an error loading data from upstream server")
      }
    }
  }
}
