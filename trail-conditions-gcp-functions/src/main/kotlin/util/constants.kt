package util

import trail.networking.MorcTrailRepository
import trail.networking.AggregatedTrailsRepository
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

// values used in each of the functions
// todo move to dependency injection and write tests
// also clean up once intellij's highlighting
// and imports start working on multiplatform

val httpClient = HttpClient()
val morcTrailRepository = MorcTrailRepository(httpClient)
val aggregatedTrailsRepository = AggregatedTrailsRepository(HttpClient())

val json = Json
