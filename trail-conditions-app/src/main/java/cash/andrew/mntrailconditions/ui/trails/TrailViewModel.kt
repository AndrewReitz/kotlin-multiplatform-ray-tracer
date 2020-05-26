package cash.andrew.mntrailconditions.ui.trails

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import trail.networking.model.MorcTrail
import trail.networking.model.TrailData
import trail.networking.model.TrailInfo

data class TrailViewModel(
  val name: String,
  val status: String,
  val description: String,
  val updatedAt: Instant?
)

fun TrailData.toViewModel() = TrailViewModel(
  name = name,
  status = status,
  description = fullDescription.trim(),
  updatedAt = LocalDateTime.parse(lastUpdated).atZone(ZoneId.systemDefault()).toInstant()
)

fun MorcTrail.toViewModel() = TrailViewModel(
  name = trailName,
  status = trailStatus ?: "Unknown", // todo should not happen once launched
  description = description.orEmpty().trim(),
  updatedAt = Instant.ofEpochMilli(updatedAt).atZone(ZoneId.systemDefault()).toInstant()
)

fun TrailInfo.toViewModel() = TrailViewModel(
  name = name,
  status = status,
  description = description,
  updatedAt = Instant.ofEpochMilli(lastUpdated).atZone(ZoneId.systemDefault()).toInstant()
)
