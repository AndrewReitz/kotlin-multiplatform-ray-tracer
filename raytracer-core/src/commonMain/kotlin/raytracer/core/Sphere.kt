@file:Suppress("NOTHING_TO_INLINE")

package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector3
import raytracer.math.toVector

data class Sphere(
  val center: Point = Point(0, 0, 0),
  val transform: Matrix = Matrix.IDENTITY,
  val material: Material = Material()
) {

  inline fun normalAt(worldPoint: Point): Vector3 {
    val objectPoint = transform.inverse() * worldPoint
    val objectNormal = objectPoint - Point(0, 0, 0)
    val worldNormal = (transform.inverse().transpose() * objectNormal).toVector()
    return worldNormal.normalize()
  }
}
