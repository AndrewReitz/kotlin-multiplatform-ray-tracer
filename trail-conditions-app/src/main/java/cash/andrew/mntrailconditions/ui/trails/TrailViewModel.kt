package cash.andrew.mntrailconditions.ui.trails

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import trail.networking.model.TrailInfo

data class TrailViewModel(
  val name: String,
  val status: String,
  val description: String,
  val updatedAt: Instant?
)

fun TrailInfo.toViewModel() = TrailViewModel(
  name = name,
  status = status,
  description = description,
  updatedAt = Instant.ofEpochMilli(lastUpdated).atZone(ZoneId.systemDefault()).toInstant()
)
