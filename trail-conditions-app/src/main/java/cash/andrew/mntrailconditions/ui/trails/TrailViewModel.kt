package cash.andrew.mntrailconditions.ui.trails

import cash.andrew.mntrailconditions.data.model.TrailData
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import trail.networking.model.MorcTrail

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

fun MorcTrail.toViewModel() = TrailViewModel(
  name = trailName,
  status = trailStatus ?: "Unknown", // todo should not happen once launched
  description = description.orEmpty().trim(),
  updatedAt = Instant.ofEpochMilli(updatedAt).atZone(ZoneId.systemDefault()).toInstant()
)
