package cash.andrew.mntrailconditions.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = true)
data class TrailData(
        val name: String,
        val status: String,
        @Json(name = "fullDescription") val description: String,
        val lastUpdated: LocalDateTime
)
