package raytracer.core.patterns

import raytracer.core.Color
import raytracer.math.Point
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class RingPatternTest {
    @JsName("A_ring_should_extend_in_both_x_and_z")
    @Test
    fun `A ring should extend in both x and z`() {
        val pattern = RingPattern(Color.White, Color.Black)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 0)), expected = Color.White)
        assertEquals(actual = pattern.patternAt(Point(1, 0, 0)), expected = Color.Black)
        assertEquals(actual = pattern.patternAt(Point(0, 0, 1)), expected = Color.Black)
        // 0.708 is just slightly more than √2/2
        assertEquals(actual = pattern.patternAt(Point(0.708, 0, 0.708)), expected = Color.Black)
    }
}
