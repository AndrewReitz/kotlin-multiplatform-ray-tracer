package raytracer.core.shapes

import raytracer.core.Ray
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlaneTest {

    @JsName("The_normal_of_a_plane_is_constant_everywhere")
    @Test
    fun `The normal of a plane is constant everywhere`() {
        val p = Plane()

        val n1 = p.localNormalAt(Point(0, 0, 0))
        val n2 = p.localNormalAt(Point(10, 0, -10))
        val n3 = p.localNormalAt(Point(-5, 0, 150))

        assertFloat3Equals(actual = n1, expected = Vector(0, 1, 0))
        assertFloat3Equals(actual = n2, expected = Vector(0, 1, 0))
        assertFloat3Equals(actual = n3, expected = Vector(0, 1, 0))
    }

    @JsName("Intersect_with_a_ray_parallel_to_the_plane")
    @Test
    fun `Intersect with a ray parallel to the plane`() {
        val p = Plane()
        val r = Ray(Point(0, 10, 0), Vector(0, 0, 1))
        val xs = p.intersect(r)
        assertTrue(xs.isEmpty())
    }

    @JsName("Intersect_with_a_coplanar_ray")
    @Test
    fun `Intersect with a coplanar ray`() {
        val p = Plane()
        val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
        val xs = p.intersect(r)
        assertTrue(xs.isEmpty())
    }

    @JsName("A_ray_intersecting_a_plane_from_above")
    @Test
    fun `A ray intersecting a plane from above`() {
        val p = Plane()
        val r = Ray(Point(0, 1, 0), Vector(0, -1, 0))
        val xs = p.intersect(r)
        assertEquals(actual = xs.size, expected = 1)
        assertEquals(actual = xs[0].time, expected = 1f)
        assertEquals(actual = xs[0].obj, expected = p)
    }

    @JsName("A_ray_intersecting_a_plane_from_below")
    @Test
    fun `A ray intersecting a plane from below`() {
        val p = Plane()
        val r = Ray(Point(0, -1, 0), Vector(0, 1, 0))
        val xs = p.localIntersect(r)
        assertEquals(actual = xs.count(), expected = 1)
        assertEquals(actual = xs[0].time, expected = 1f)
        assertEquals(actual = xs[0].obj, expected = p)
    }
}
