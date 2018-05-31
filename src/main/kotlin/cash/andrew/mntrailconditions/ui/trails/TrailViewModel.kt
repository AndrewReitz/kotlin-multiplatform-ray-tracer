package cash.andrew.mntrailconditions.ui.trails

import cash.andrew.mntrailconditions.data.model.TrailDataV2
import cash.andrew.mntrailconditions.data.model.TrailDataV3
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

data class TrailViewModel(
        val name: String,
        val status: String,
        val description: String,
        val updatedAt: Instant?
)

fun TrailDataV2.toViewModel() = TrailViewModel(
        name = name,
        status = status,
        description = description.trim(),
        updatedAt = lastUpdated.atZone(ZoneId.systemDefault()).toInstant()
)

fun TrailDataV3.toViewModel() = TrailViewModel(
        name = trailName ?: "",
        status = trailStatus ?: "Unknown",
        description = description ?: "",
        updatedAt = createdAt
)
