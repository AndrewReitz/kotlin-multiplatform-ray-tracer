package cash.andrew.mntrailconditions.data.model

import android.support.annotation.DrawableRes
import cash.andrew.mntrailconditions.MnTrailConditionsApp
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.api.TrailConditionsService
import cash.andrew.mntrailconditions.util.statusToResource
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

data class TrailInfo(
        val name: String,
        val status: String,
        val description: String,
        val lastUpdated: LocalDateTime
)

val TrailInfo.lastUpdatedFormatted: String get() = MnTrailConditionsApp.DATE_TIME_FORMAT.format(lastUpdated)

@get:DrawableRes val TrailInfo.resourceId: Int get() = statusToResource(status)
