package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector4

data class Sphere(
  val center: Point = Point(0, 0, 0),
  val transform: Matrix = Matrix.IDENTITY
)
