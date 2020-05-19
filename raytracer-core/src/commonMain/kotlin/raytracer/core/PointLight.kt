package raytracer.core

import raytracer.math.Point

data class PointLight(
  val position: Point,
  val intensity: Color
)
