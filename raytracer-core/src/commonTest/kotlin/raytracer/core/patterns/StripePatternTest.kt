package raytracer.core.patterns

import raytracer.core.Color
import raytracer.core.shapes.Sphere
import raytracer.math.Matrix
import raytracer.math.Point
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class StripePatternTest {

    @JsName("Creating_a_stripe_pattern")
    @Test
    fun `Creating a stripe pattern`() {
        val pattern = StripePattern(Color.White, Color.Black)
        assertEquals(actual = pattern.a, expected = Color.White)
        assertEquals(actual = pattern.b, expected = Color.Black)
    }

    @JsName("A_stripe_pattern_is_constant_in_y")
    @Test
    fun `A stripe pattern is constant in Y`() {
        val pattern = StripePattern(Color.White, Color.Black)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0, 1, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0, 2, 0)), expected = Color.White)
    }

    @JsName("A_stripe_pattern_is_constant_in_z")
    @Test
    fun `A stripe pattern is constant in z`() {
        val pattern = StripePattern(Color.White, Color.Black)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 1)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 2)), expected = Color.White)
    }

    @JsName("A_stripe_pattern_alternates_in_x")
    @Test
    fun `A stripe pattern alternates in x`() {
        val pattern = StripePattern(Color.White, Color.Black)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0.9, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(1, 0, 0)), expected = Color.Black)
        assertEquals(actual = pattern.patternAt(Point(-0.1, 0, 0)), expected = Color.Black)
        assertEquals(actual = pattern.patternAt(Point(-1, 0, 0)), expected = Color.Black)
        assertEquals(actual = pattern.patternAt(Point(-1.1, 0, 0)), expected = Color.White)
    }

    @JsName("Stripes_with_an_object_transformation")
    @Test
    fun `Stripes with an object transformation`() {
        val obj = Sphere(transform = Matrix.scaling(2, 2, 2))
        val pattern = StripePattern(Color.White, Color.Black)
        val c = pattern.patternAtShape(obj, Point(1.5, 0, 0))
        assertEquals(actual = c, expected = Color.White)
    }

    @JsName("Stripes_with_a_pattern_transformation")
    @Test
    fun `Stripes with a pattern transformation`() {
        val obj = Sphere()
        val pattern = StripePattern(a = Color.White, b = Color.Black, transform = Matrix.scaling(2, 2, 2))
        val c = pattern.patternAtShape(obj, Point(1.5, 0, 0))
        assertEquals(actual = c, expected = Color.White)
    }

    @JsName("Stripes_with_both_an_object_and_a_pattern_transformation")
    @Test
    fun `Stripes with both an object and a pattern transformation`() {
        val obj = Sphere(transform = Matrix.scaling(2, 2, 2))
        val pattern = StripePattern(a = Color.White, b = Color.Black, transform = Matrix.translation(0.5, 0, 0))
        val c = pattern.patternAtShape(obj, Point(2.5, 0, 0))
        assertEquals(actual = c, expected = Color.White)
    }
}
