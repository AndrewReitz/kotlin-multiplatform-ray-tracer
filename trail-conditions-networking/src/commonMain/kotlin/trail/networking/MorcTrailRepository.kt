package trail.networking

import cash.andrew.kotlin.common.Result
import cash.andrew.kotlin.common.resultFrom
import cash.andrew.kotlin.common.retry
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import trail.networking.model.MorcTrail

class MorcTrailRepository(
    client: HttpClient,
    private val url: String = "https://api.morcmtb.org/v1/trails"
) {

    private val client = client.config {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getTrails(): Result<List<MorcTrail>, Exception> = resultFrom {
        retry {
            client.get<List<MorcTrail>>(url)
        }
    }
}

