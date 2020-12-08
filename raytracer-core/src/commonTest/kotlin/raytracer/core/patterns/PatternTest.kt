package raytracer.core.patterns

import raytracer.core.Color
import raytracer.core.shapes.Sphere
import raytracer.math.Matrix
import raytracer.math.Point
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class TestPattern(
    override val transform: Matrix = Matrix.IDENTITY
) : Pattern {
    override fun patternAt(point: Point): Color {
        return Color(point.x, point.y, point.z)
    }
}

class PatternTest {

    @JsName("The_default_pattern_transformation")
    @Test
    fun `The default pattern transformation`() {
        val pattern = TestPattern()
        assertEquals(actual = pattern.transform, expected = Matrix.IDENTITY)
    }

    @JsName("Assigning_a_transformation")
    @Test
    fun `Assigning a transformation`() {
        val expected = Matrix.translation(1, 2, 3)
        val pattern = TestPattern(transform = expected)
        assertEquals(actual = pattern.transform, expected = expected)
    }

    @JsName("A_pattern_with_an_object_transformation")
    @Test
    fun `A pattern with an object transformation`() {
        val shape = Sphere(transform = Matrix.scaling(2, 2, 2))
        val pattern = TestPattern()
        val c = pattern.patternAtShape(shape, Point(2, 3, 4))
        assertEquals(actual = c, expected = Color(1, 1.5, 2))
    }

    @JsName("A_pattern_with_both_an_object_and_a_pattern_transformation")
    @Test
    fun `A pattern with both an object and a pattern transformation`() {
        val shape = Sphere(transform = Matrix.scaling(2, 2, 2))
        val pattern = TestPattern(transform = Matrix.translation(0.5, 1, 1.5))
        val c = pattern.patternAtShape(shape, Point(2.5, 3, 3.5))
        assertEquals(actual = c, expected = Color(0.75, 0.5, 0.25))
    }
}
