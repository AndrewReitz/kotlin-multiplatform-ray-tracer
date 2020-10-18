package trail.networking.model

import java.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TrailInfo(
  val name: String,
  val description: String,
  val status: String,
  val lastUpdated: Long,
  val facebookUrl: String? = null,
  val mtbProjectUrl: String? = null,
  val twitterAccount: String? = null
) {

  companion object {
    fun createUnknownStatus(trail: KnownTrail) = TrailInfo(
      name = trail.trailName,
      status = "Unknown",
      description = "There was an issue loading the trail status",
      lastUpdated =  Instant.now().toEpochMilli(),
      mtbProjectUrl = trail.mtbProjectUrl,
      facebookUrl = trail.facebookUrl,
      twitterAccount = trail.twitterAccount?.accountName
    )
  }
}
