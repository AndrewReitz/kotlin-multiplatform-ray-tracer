package trail.networking

import cash.andrew.kotlin.common.Result
import cash.andrew.kotlin.common.resultFrom
import cash.andrew.kotlin.common.retry
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.withTimeout
import trail.networking.model.MorcTrail

class MorcTrailRepository(
    client: HttpClient,
    private val url: String = "https://us-central1-mn-trail-functions.cloudfunctions.net/morcTrails"
    // "https://api.morcmtb.org/v1/trails" should be released here when it's published
) {

    private val client = client.config {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getTrails(timeout: Long = 3000): Result<List<MorcTrail>, Exception> = resultFrom {
        retry {
            withTimeout(timeout) {
                client.get<List<MorcTrail>>(url)
            }
        }
    }
}

