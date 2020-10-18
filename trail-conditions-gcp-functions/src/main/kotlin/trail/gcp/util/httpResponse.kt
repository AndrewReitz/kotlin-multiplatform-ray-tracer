package trail.gcp.util

import com.google.cloud.functions.HttpResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
inline fun <reified T> HttpResponse.sendJson(data: T, maxCacheAge: Duration) {
  appendHeader("Cache-Control", "max-age=${maxCacheAge.inSeconds}")
  appendHeader("Content-Type", "application/json; charset=utf-8")
  writer.write(Json.encodeToString(data))
}
