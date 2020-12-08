package raytracer.core.patterns

import raytracer.core.Color
import raytracer.math.Point
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.test.Test

class GradientPatternTest {
    @JsName("A_gradient_linearly_interpolates_between_colors")
    @Test
    fun `A gradient linearly interpolates between colors`() {
        val pattern = GradientPattern(Color.White, Color.Black)
        assertFloat3Equals(actual = pattern.patternAt(Point(0, 0, 0)), expected = Color.White)
        assertFloat3Equals(actual = pattern.patternAt(Point(0.25, 0, 0)), expected = Color(0.75, 0.75, 0.75))
        assertFloat3Equals(actual = pattern.patternAt(Point(0.5, 0, 0)), expected = Color(0.5, 0.5, 0.5))
        assertFloat3Equals(actual = pattern.patternAt(Point(0.75, 0, 0)), expected = Color(0.25, 0.25, 0.25))
    }
}
