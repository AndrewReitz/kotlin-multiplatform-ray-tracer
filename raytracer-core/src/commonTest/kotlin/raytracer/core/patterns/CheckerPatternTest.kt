package raytracer.core.patterns

import raytracer.core.Color
import raytracer.math.Point
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckerPatternTest {
    @JsName("Checkers_should_repeat_in_x")
    @Test
    fun `Checkers should repeat in x`() {
        val pattern = CheckerPattern(Color.White, Color.Black)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0.99, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(1.01, 0, 0)), expected = Color.Black)
    }

    @JsName("Checkers_should_repeat_in_y")
    @Test
    fun `Checkers should repeat in y`() {
        val pattern = CheckerPattern(Color.White, Color.Black)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0, 0.99, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0, 1.01, 0)), expected = Color.Black)
    }

    @JsName("Checkers_should_repeat_in_z")
    @Test
    fun `Checkers should repeat in z`() {
        val pattern = CheckerPattern(Color.White, Color.Black)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 0.99)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 1.01)), expected = Color.Black)
    }
}
