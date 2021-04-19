package raytracer.core

import kotlinx.serialization.Serializable
import raytracer.math.Point

@Serializable
data class PointLight(
    val position: Point,
    val intensity: Color
)
