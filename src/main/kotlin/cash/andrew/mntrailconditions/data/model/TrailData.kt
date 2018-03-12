package cash.andrew.mntrailconditions.data.model

import android.support.annotation.DrawableRes
import cash.andrew.mntrailconditions.MnTrailConditionsApp
import cash.andrew.mntrailconditions.util.statusToResource
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

data class TrailData(
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

val TrailData.updatedAtString: String get() = MnTrailConditionsApp.DATE_TIME_FORMAT.format(
        LocalDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
)

@get:DrawableRes
val TrailData.resourceId: Int get() = statusToResource(trailStatus ?: "")
