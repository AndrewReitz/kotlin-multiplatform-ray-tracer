package raytracer.core

import raytracer.core.shapes.GlassSphere
import raytracer.math.Point
import raytracer.math.SQUARE_ROOT_OF_2_OVER_2
import raytracer.math.Vector
import raytracer.test.assertFloatsEquals
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ComputationTest {
    @JsName("The_Schlick_approximation_under_total_internal_reflection")
    @Test
    fun `The Schlick approximation under total internal reflection`() {
        val shape = GlassSphere()
        val r = Ray(Point(0, 0, SQUARE_ROOT_OF_2_OVER_2), Vector(0, 1, 0))
        val xs = Intersections(
            Intersection(-SQUARE_ROOT_OF_2_OVER_2, shape),
            Intersection(SQUARE_ROOT_OF_2_OVER_2, shape)
        )
        val comps = xs.last().prepareComputations(r, xs)
        val reflectance = comps.schlick
        assertEquals(actual = reflectance, expected = 1f)
    }

    @JsName("The_Schlick_approximation_with_a_perpendicular_viewing_angle")
    @Test
    fun `The Schlick approximation with a perpendicular viewing angle`() {
        val shape = GlassSphere()
        val r = Ray(Point(0, 0, 0), Vector(0, 1, 0))
        val xs = Intersections(Intersection(-1, shape), Intersection(1, shape))
        val comps = xs.last().prepareComputations(r, xs)
        val reflectance = comps.schlick
        assertFloatsEquals(actual = reflectance, expected = 0.04)
    }

    @JsName("The_Schlick_approximation_with_small_angle_and_n2_greater_than_n1")
    @Test
    fun `The Schlick approximation with small angle and n2 greater than n1`() {
        val shape = GlassSphere()
        val r = Ray(Point(0, 0.99, -2), Vector(0, 0, 1))
        val xs = Intersections(Intersection(1.8589, shape))
        val comps = xs.first().prepareComputations(r, xs)
        val reflectance = comps.schlick
        assertFloatsEquals(actual = reflectance, expected = 0.48873)
    }
}
