package trail.networking

import cash.andrew.kotlin.common.Result
import cash.andrew.kotlin.common.resultFrom
import cash.andrew.kotlin.common.retry
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.withTimeout
import trail.networking.model.TrailData

/** Old Api that scrapes the website. Swap to [MorcTrailRepository] when available. */
class HtmlMorcTrailRepository(
    client: HttpClient,
    private val url: String = "https://mn-trail-info-service.herokuapp.com/"
) {

    private val client = client.config {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getTrails(timeout: Long = 3000): Result<List<TrailData>, Exception> = resultFrom {
        retry {
            withTimeout(timeout) {
                client.get<List<TrailData>>(url) {
                    header("api-version", "2")
                }
            }
        }
    }
}

