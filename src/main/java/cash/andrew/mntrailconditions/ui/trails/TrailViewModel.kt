package cash.andrew.mntrailconditions.ui.trails

import cash.andrew.mntrailconditions.data.model.TrailData
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

data class TrailViewModel(
        val name: String,
        val status: String,
        val description: String,
        val updatedAt: Instant?
)

fun TrailData.toViewModel() = TrailViewModel(
        name = name,
        status = status,
        description = description.trim(),
        updatedAt = lastUpdated.atZone(ZoneId.systemDefault()).toInstant()
)
