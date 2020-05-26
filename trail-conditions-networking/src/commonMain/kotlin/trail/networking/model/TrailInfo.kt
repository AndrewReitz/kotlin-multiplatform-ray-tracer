package trail.networking.model

import kotlinx.serialization.Serializable
import trail.networking.dateTimeNowAsLong

@Serializable
data class TrailInfo(
  val name: String,
  val description: String,
  val status: String,
  val lastUpdated: Long,
  val facebookUrl: String?,
  val mtbProjectUrl: String?
) {
  companion object {
    fun createUnknownStatus(trail: KnownTrail) = TrailInfo(
      name = trail.trailName,
      status = "Unknown",
      description = "There was an issue loading the trail status",
      lastUpdated =  dateTimeNowAsLong(),
      mtbProjectUrl = trail.mtbProjectUrl,
      facebookUrl = trail.facebookUrl
    )
  }
}
