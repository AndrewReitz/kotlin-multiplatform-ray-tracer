package raytracer.core.patterns

import raytracer.core.Color
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.isEven
import kotlin.math.floor
import kotlin.math.sqrt

class RingPattern(
    private val a: Color,
    private val b: Color,
    override val transform: Matrix = Matrix.IDENTITY
) : Pattern {
    override fun patternAt(point: Point): Color {
        return if (floor(sqrt(point.x * point.x + point.z * point.z)).toInt().isEven) a else b
    }
}
