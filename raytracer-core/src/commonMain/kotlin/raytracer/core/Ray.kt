@file:Suppress("NOTHING_TO_INLINE")

package raytracer.core

import raytracer.math.Tuple
import kotlin.math.pow
import kotlin.math.sqrt

data class Ray(
  val origin: Tuple,
  val direction: Tuple
) {
  inline fun position(time: Number): Tuple = origin + direction * time.toFloat()

  inline fun intersects(sphere: Sphere): Intersections {
    val sphereToRay = origin - sphere.center

    val a = direction dot direction
    val b = 2 * (direction dot sphereToRay)
    val c = (sphereToRay dot sphereToRay) - 1f

    val discriminant = b.pow(2) - 4f * a * c

    if (discriminant < 0f) return Intersections.EMPTY

    val t1 = (-b - sqrt(discriminant)) / (2f * a)
    val t2 = (-b + sqrt(discriminant)) / (2f * a)

    return intersectionsOf(Intersection(t1, sphere), Intersection(t2, sphere))
  }
}
