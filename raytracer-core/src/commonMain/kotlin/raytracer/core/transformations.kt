package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Matrix4
import raytracer.math.Point
import raytracer.math.Vector3
import raytracer.math.toVector

fun viewTransform(from: Point, to: Point, up: Vector3): Matrix {
  val forward = (to - from).toVector().normalize()
  val upn = up.normalize()
  val left = forward.cross(upn)
  val trueUp = left.cross(forward)

  val orientation = Matrix4 {
    r1(left.x, left.y, left.z, 0)
    r2(trueUp.x, trueUp.y, trueUp.z, 0)
    r3(-forward.x, -forward.y, -forward.z, 0)
    r4(0, 0, 0, 1)
  }

  return orientation * Matrix.translation(-from.x, -from.y, -from.z)
}
