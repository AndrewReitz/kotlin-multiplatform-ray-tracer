package cash.andrew.mntrailconditions.data.model

import cash.andrew.mntrailconditions.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime

@JsonClass(generateAdapter = BuildConfig.MOSHI_GENERATOR_ENABLED)
data class TrailDataV2(
        val name: String,
        val status: String,
        @Json(name = "fullDescription") val description: String,
        val lastUpdated: LocalDateTime
)
