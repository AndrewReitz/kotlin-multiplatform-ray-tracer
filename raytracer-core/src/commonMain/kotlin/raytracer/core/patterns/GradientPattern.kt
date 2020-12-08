package raytracer.core.patterns

import raytracer.core.Color
import raytracer.math.Matrix
import raytracer.math.Point
import kotlin.math.floor

class GradientPattern(
    private val a: Color,
    private val b: Color,
    override val transform: Matrix = Matrix.IDENTITY
) : Pattern {
    override fun patternAt(point: Point): Color {
        val distance = b - a
        val fraction = point.x - floor(point.x)
        return a + distance * fraction
    }
}
