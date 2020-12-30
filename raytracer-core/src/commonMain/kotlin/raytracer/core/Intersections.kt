package raytracer.core

fun Intersections(vararg intersections: Intersection) = Intersections(intersections.toList())

class Intersections constructor(
    private val intersections: List<Intersection>
) : List<Intersection> by intersections {

    companion object {
        val EMPTY = Intersections(emptyList())
    }

    fun hit(): Intersection? = intersections.filter { it.time > 0 }
        .minByOrNull { it.time }
}
