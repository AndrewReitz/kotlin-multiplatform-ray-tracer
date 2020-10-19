package trail.networking.model

import kotlinx.serialization.Serializable

/** Models for Html Parsing API */
@Serializable
data class TrailData(
    val name: String,
    val status: String,
    val description: String,
    val fullDescription: String,
    // "2019-08-19T09:20"
    val lastUpdated: String
)
