@file:Suppress("FunctionName", "NOTHING_TO_INLINE")

package raytracer.core

data class Intersection(val time: Float, val obj: Sphere) {
  fun prepareComputations(ray: Ray): Computation {
    val point = ray.position(time)
    val normal = obj.normalAt(point)
    val eye = -ray.direction
    val inside = normal dot eye < 0

    return Computation(
      time = time,
      obj = obj,
      point = point,
      eyev = -ray.direction,
      normalv = if (inside) -normal else normal,
      inside = inside
    )
  }
}

fun Intersection(time: Number, obj: Sphere) = Intersection(time.toFloat(), obj)
