import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

external class Response {
  fun setHeader(key: String, value: String)
  fun send(data: String)
  fun status(code: Int)
}

@ExperimentalTime
fun <T> Response.json(data: T, serializer: SerializationStrategy<T>, maxCacheAge: Duration) {
  setHeader("Cache-Control", "max-age=${maxCacheAge.inSeconds}")
  setHeader("Content-Type", "application/json; charset=utf-8")
  send(Json(JsonConfiguration.Stable).stringify(serializer, data))
}
