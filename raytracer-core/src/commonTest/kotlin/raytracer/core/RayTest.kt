package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class RayTest {
    @JsName("Creating_and_querying_a_ray")
    @Test
    fun `Creating and querying a ray`() {
        val origin = Point(1, 2, 3)
        val direction = Vector(4, 5, 6)

        val r = Ray(origin = origin, direction = direction)
        assertEquals(actual = r.origin, expected = origin)
        assertEquals(actual = r.direction, expected = direction)
    }

    @JsName("Computing_a_point_from_a_distance")
    @Test
    fun `Computing a point from a distance`() {
        val r = Ray(origin = Point(2, 3, 4), direction = Vector(1, 0, 0))
        assertFloat3Equals(actual = r.position(0), expected = Point(2, 3, 4))
        assertFloat3Equals(actual = r.position(1), expected = Point(3, 3, 4))
        assertFloat3Equals(actual = r.position(-1), expected = Point(1, 3, 4))
        assertFloat3Equals(actual = r.position(2.5), expected = Point(4.5, 3, 4))
    }

    @JsName("Translating_a_ray")
    @Test
    fun `Translating a ray`() {
        val r = Ray(Point(1, 2, 3), Vector(0, 1, 0))
        val m = Matrix.translation(3, 4, 5)
        val r2 = r.transform(m)
        assertFloat3Equals(actual = r2.origin, expected = Point(4, 6, 8))
        assertFloat3Equals(actual = r2.direction, expected = Vector(0, 1, 0))
    }

    @JsName("Scaling_a_ray")
    @Test
    fun `Scaling a ray`() {
        val r = Ray(Point(1, 2, 3), Vector(0, 1, 0))
        val m = Matrix.scaling(2, 3, 4)
        val r2 = r.transform(m)
        assertFloat3Equals(actual = r2.origin, expected = Point(2, 6, 12))
        assertFloat3Equals(actual = r2.direction, expected = Vector(0, 3, 0))
    }
}
