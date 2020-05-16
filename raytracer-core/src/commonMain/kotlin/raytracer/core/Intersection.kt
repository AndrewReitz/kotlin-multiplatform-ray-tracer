@file:Suppress("FunctionName", "NOTHING_TO_INLINE")

package raytracer.core

data class Intersection(val time: Float, val obj: Sphere)

fun Intersection(time: Number, obj: Sphere) = Intersection(time.toFloat(), obj)

data class Intersections @PublishedApi internal constructor(
  @PublishedApi internal val intersections: Array<out Intersection>
) {

  val size: Int = intersections.size
  operator fun get(index: Int): Intersection = intersections[index]
  operator fun iterator(): Iterator<Intersection> = intersections.iterator()
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as Intersections

    if (!intersections.contentEquals(other.intersections)) return false

    return true
  }

  override fun hashCode(): Int {
    return intersections.contentHashCode()
  }

  inline fun hit(): Intersection? {
    if (intersections.isEmpty()) return null

    val first = intersections[0]
    if (first.time <= 0) return null
    return intersections[0]
  }

  companion object {
    val EMPTY = Intersections(emptyArray())
  }
}

inline fun intersectionsOf(vararg intersections: Intersection) =
  Intersections(
    intersections.apply {
      // sort by lowest value closest to 0 but not under
      sortBy { if (it.time >= 0f) it.time else Float.MAX_VALUE }
    }
  )
