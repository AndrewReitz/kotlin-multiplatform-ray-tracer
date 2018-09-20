package cash.andrew.mntrailconditions.data.model

import cash.andrew.mntrailconditions.BuildConfig
import com.squareup.moshi.JsonClass
import org.threeten.bp.Instant

@JsonClass(generateAdapter = BuildConfig.MOSHI_GENERATOR_ENABLED)
data class TrailDataV3(
        val city: String?,
        val trailName: String?,
        val zipcode: String?,
        val trailStatus: String?,
        val trailId: String?,
        val updatedAt: Instant?,
        val longitude: Double?,
        val createdAt: Instant?,
        val description: String?,
        val latitude: Double?,
        val state: String?,
        val street: String?
)
