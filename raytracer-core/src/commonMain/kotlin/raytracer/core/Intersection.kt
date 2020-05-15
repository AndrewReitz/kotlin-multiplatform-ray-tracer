@file:Suppress("FunctionName")

package raytracer.core

data class Intersection(val time: Float, val obj: Sphere)

fun Intersection(time: Number, obj: Sphere) = Intersection(time.toFloat(), obj)

data class Intersections(
  private val intersections: Array<Intersection>
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

  companion object {
    val EMPTY = Intersections(emptyArray())
  }
}

fun Intersections(i1: Intersection, i2: Intersection) = Intersections(arrayOf(i1, i2))
