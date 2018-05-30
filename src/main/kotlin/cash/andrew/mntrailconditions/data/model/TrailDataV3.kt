package cash.andrew.mntrailconditions.data.model

import android.support.annotation.DrawableRes
import cash.andrew.mntrailconditions.util.statusToResource
import com.squareup.moshi.JsonClass
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit

@JsonClass(generateAdapter = true)
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

val TrailDataV3.updatedAtString: String get() {
    val now = LocalDateTime.now()
    val days = ChronoUnit.DAYS.between(updatedAt, now)
    val hours = ChronoUnit.HOURS.between(updatedAt, now) % 24
    return if (days == 0L) "$hours hours ago" else "$days days $hours hours ago"
}

@get:DrawableRes
val TrailDataV3.resourceId: Int get() = statusToResource(trailStatus ?: "")
