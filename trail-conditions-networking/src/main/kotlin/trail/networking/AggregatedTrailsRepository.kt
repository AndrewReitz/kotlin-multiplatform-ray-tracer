package trail.networking

import cash.andrew.kotlin.common.Result
import cash.andrew.kotlin.common.resultFrom
import cash.andrew.kotlin.common.retry
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.withTimeout
import trail.networking.model.TrailInfo

class AggregatedTrailsRepository(
  client: HttpClient,
  private val url: String = "https://us-central1-mn-trail-functions.cloudfunctions.net/trail-aggregator"
) {

  private val client = client.config {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
  }

  suspend fun getTrails(timeout: Long = 3000): Result<List<TrailInfo>, Exception> = resultFrom {
    retry {
      withTimeout(timeout) {
        client.get(url)
      }
    }
  }
}
