package trail.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class MorcTrail(
  val trailName: String,
  val trailId: String,
  val createdAt: Long,
  val updatedBy: String,
  val state: String,
  val city: String,
  val zipcode: String,
  val trailStatus: String?,
  val updatedAt: Long,
  val longitude: String,
  val description: String?,
  val latitude: String,
  val street: String
)
