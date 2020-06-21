package raytracer.core

@Suppress("FunctionName")
fun Intersections(vararg intersections: Intersection) = Intersections(
  intersections.toMutableList().apply {
    // sort by lowest value closest to 0 but not under
    sortBy { if (it.time >= 0f) it.time else Float.MAX_VALUE }
  }
)

data class Intersections(
  private val intersections: List<Intersection>
) : List<Intersection> by intersections {

  companion object {
    val EMPTY = Intersections(emptyList())
  }

  fun hit(): Intersection? {
    if (intersections.isEmpty()) return null

    val first = intersections[0]
    if (first.time <= 0) return null
    return intersections[0]
  }
}
