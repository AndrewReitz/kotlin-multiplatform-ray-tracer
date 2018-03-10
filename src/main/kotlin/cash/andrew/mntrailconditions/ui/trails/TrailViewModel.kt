package cash.andrew.mntrailconditions.ui.trails

import android.support.annotation.DrawableRes
import cash.andrew.mntrailconditions.data.model.TrailData
import cash.andrew.mntrailconditions.data.model.TrailInfo
import cash.andrew.mntrailconditions.data.model.lastUpdatedFormatted
import cash.andrew.mntrailconditions.data.model.resourceId
import cash.andrew.mntrailconditions.data.model.updatedAtString

data class TrailViewModel(
        val name: String,
        val status: String,
        val description: String,
        val lastUpdated: String,
        @DrawableRes val statusDrawableId: Int
)

fun TrailInfo.toViewModel() = TrailViewModel(
        name = name,
        status = status,
        description = description,
        lastUpdated = lastUpdatedFormatted,
        statusDrawableId = resourceId
)

fun TrailData.toViewModel() = TrailViewModel(
        name = trailName ?: "",
        status = trailStatus ?: "",
        description = description ?: "",
        lastUpdated = updatedAtString,
        statusDrawableId = resourceId
)
