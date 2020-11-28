package raytracer.core

import raytracer.core.shapes.Sphere
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HitTest {
    @JsName("The_hit_when_all_intersections_have_positive_time")
    @Test
    fun `The hit, when all intersections have positive time`() {
        val s = Sphere()
        val i1 = Intersection(1, s)
        val i2 = Intersection(2, s)
        val xs = Intersections(i2, i1)
        val i = xs.hit()
        assertEquals(actual = i, expected = i1)
    }

    @JsName("The_hit_when_some_intersections_have_negative_time")
    @Test
    fun `The hit, when some intersections have negative time`() {
        val s = Sphere()
        val i1 = Intersection(-1, s)
        val i2 = Intersection(1, s)
        val xs = Intersections(i2, i1)
        val i = xs.hit()
        assertEquals(actual = i, expected = i2)
    }

    @JsName("The_hit_when_all_intersections_have_negative_time")
    @Test
    fun `The hit, when all intersections have negative time`() {
        val s = Sphere()
        val i1 = Intersection(-2, s)
        val i2 = Intersection(-1, s)
        val xs = Intersections(i2, i1)
        val i = xs.hit()
        assertNull(i)
    }

    @JsName("The_hit_is_always_the_lowest_nonnegative_intersection")
    @Test
    fun `The hit is always the lowest nonnegative intersection`() {
        val s = Sphere()
        val i1 = Intersection(5, s)
        val i2 = Intersection(7, s)
        val i3 = Intersection(-3, s)
        val i4 = Intersection(2, s)

        val xs = Intersections(i1, i2, i3, i4)
        val i = xs.hit()
        assertEquals(actual = i, expected = i4)
    }
}
