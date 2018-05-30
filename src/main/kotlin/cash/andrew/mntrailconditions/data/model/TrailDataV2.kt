package cash.andrew.mntrailconditions.data.model

import android.support.annotation.DrawableRes
import cash.andrew.mntrailconditions.util.statusToResource
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit

@JsonClass(generateAdapter = true)
data class TrailDataV2(
        val name: String,
        val status: String,
        @Json(name = "fullDescription") val description: String,
        val lastUpdated: LocalDateTime
)

val TrailDataV2.lastUpdatedFormatted: String get() {
    val now = LocalDateTime.now()
    val days = ChronoUnit.DAYS.between(lastUpdated, now)
    val hours = ChronoUnit.HOURS.between(lastUpdated, now) % 24
    return if (days == 0L) "$hours hours ago" else "$days days $hours hours ago"
}

@get:DrawableRes
val TrailDataV2.resourceId: Int get() = statusToResource(status)
