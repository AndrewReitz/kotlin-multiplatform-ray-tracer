package raytracer.core

import raytracer.math.Point
import raytracer.math.Vector3

data class Computation(
  val time: Float,
  val obj: Sphere,
  val point: Point,
  val eyev: Vector3,
  val normalv: Vector3,
  val inside: Boolean
)
