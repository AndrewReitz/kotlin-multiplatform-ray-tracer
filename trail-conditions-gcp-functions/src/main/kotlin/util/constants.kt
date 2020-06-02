package util

import trail.networking.MorcTrailRepository
import trail.networking.HtmlMorcTrailRepository
import trail.networking.AggregatedTrailsRepository
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

// values used in each of the functions
// todo move to dependency injection and write tests
// also clean up once intellij's highlighting
// and imports start working on multiplatform

val httpClient = HttpClient()
val morcTrailRepository = MorcTrailRepository(httpClient)
val htmlMorcTrailRepository = HtmlMorcTrailRepository(httpClient)
val aggregatedTrailsRepository = AggregatedTrailsRepository(HttpClient())

val json = Json(JsonConfiguration.Stable)
