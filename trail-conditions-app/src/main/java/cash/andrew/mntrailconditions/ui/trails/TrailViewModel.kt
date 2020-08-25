package cash.andrew.mntrailconditions.ui.trails

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import trail.networking.model.KnownTrail
import trail.networking.model.TrailInfo

data class TrailViewModel(
  val name: String,
  val status: String,
  val description: String,
  val updatedAt: LocalDateTime,
  val twitterAccount: String?,
  val mountainProjectUrl: String?,
  val facebookUrl: String?
) {
  val twitterUrl get() = twitterAccount?.let { "https://twitter.com/$it" }
}

private val ZONE_ID = ZoneId.of("America/Chicago")

fun TrailInfo.toViewModel() = TrailViewModel(
  name = name,
  status = status,
  description = description,
  updatedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastUpdated), ZONE_ID),
  twitterAccount = twitterAccount,
  facebookUrl = facebookUrl,
  mountainProjectUrl = mtbProjectUrl
)
