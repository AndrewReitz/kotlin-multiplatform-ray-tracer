@file:Suppress("FunctionName", "NOTHING_TO_INLINE")

package raytracer.core

data class Intersection(val time: Float, val obj: Sphere)

fun Intersection(time: Number, obj: Sphere) = Intersection(time.toFloat(), obj)

data class Intersections(
  private val intersections: List<Intersection>
) : Iterable<Intersection> {

  companion object {
    val EMPTY = Intersections(emptyList())
  }

  val size: Int = intersections.size
  operator fun get(index: Int): Intersection = intersections[index]
  override operator fun iterator(): Iterator<Intersection> = intersections.iterator()

  fun hit(): Intersection? {
    if (intersections.isEmpty()) return null

    val first = intersections[0]
    if (first.time <= 0) return null
    return intersections[0]
  }
}

fun Intersections(vararg intersections: Intersection) = Intersections(
  intersections.toMutableList().apply {
    // sort by lowest value closest to 0 but not under
    sortBy { if (it.time >= 0f) it.time else Float.MAX_VALUE }
  }
)
