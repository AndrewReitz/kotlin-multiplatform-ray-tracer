package cash.andrew.mntrailconditions.data.model

import android.support.annotation.DrawableRes
import cash.andrew.mntrailconditions.DATE_TIME_FORMAT
import cash.andrew.mntrailconditions.util.statusToResource
import org.threeten.bp.LocalDateTime

data class TrailInfo(
        val name: String,
        val status: String,
        val description: String,
        val lastUpdated: LocalDateTime
)

val TrailInfo.lastUpdatedFormatted: String get() = DATE_TIME_FORMAT.format(lastUpdated)

@get:DrawableRes val TrailInfo.resourceId: Int get() = statusToResource(status)
