package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector3
import kotlin.math.pow
import kotlin.math.sqrt

data class Ray(
  val origin: Point,
  val direction: Vector3
) {
  fun position(time: Number): Point = origin + direction * time.toFloat()

  fun intersects(sphere: Sphere): Intersections {
    val (origin, direction) = transform(sphere.transform.inverse())
    val sphereToRay = origin - sphere.center

    val a = direction dot direction
    val b = 2 * (direction dot sphereToRay)
    val c = (sphereToRay dot sphereToRay) - 1f

    val discriminant = b * b - 4f * a * c

    if (discriminant < 0f) return Intersections.EMPTY

    val t1 = (-b - sqrt(discriminant)) / (2f * a)
    val t2 = (-b + sqrt(discriminant)) / (2f * a)

    return Intersections(Intersection(t1, sphere), Intersection(t2, sphere))
  }

  fun transform(transform: Matrix): Ray = Ray(
    origin = transform * origin,
    direction = transform * direction
  )
}
