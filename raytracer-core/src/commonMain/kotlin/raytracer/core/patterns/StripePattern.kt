package raytracer.core.patterns

import raytracer.core.Color
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.isEven
import kotlin.math.floor

data class StripePattern(
    val a: Color,
    val b: Color,
    override val transform: Matrix = Matrix.IDENTITY
) : Pattern {
    override fun patternAt(point: Point): Color = if (floor(point.x).toInt().isEven) a else b
}
