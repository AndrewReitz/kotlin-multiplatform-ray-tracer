package raytracer.core.shapes

import raytracer.core.Ray
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class SphereTest {
    @JsName("The_normal_on_a_sphere_at_a_point_on_the_x_axis")
    @Test
    fun `The normal on a sphere at a point on the x axis`() {
        val s = Sphere()
        val n = s.localNormalAt(Point(1, 0, 0))
        assertEquals(actual = n, expected = Vector(1, 0, 0))
    }

    @JsName("The_normal_on_a_sphere_at_a_point_on_the_y_axis")
    @Test
    fun `The normal on a sphere at a point on the y axis`() {
        val s = Sphere()
        val n = s.localNormalAt(Point(0, 1, 0))
        assertEquals(actual = n, expected = Vector(0, 1, 0))
    }

    @JsName("The_normal_on_a_sphere_at_a_point_on_the_z_axis")
    @Test
    fun `The normal on a sphere at a point on the z axis`() {
        val s = Sphere()
        val n = s.localNormalAt(Point(0, 0, 1))
        assertEquals(actual = n, expected = Vector(0, 0, 1))
    }

    @JsName("The_normal_on_a_sphere_at_a_nonaxial_point")
    @Test
    fun `The normal on a sphere at a nonaxial point`() {
        val s = Sphere()
        val n = s.localNormalAt(Point(sqrt(3f) / 3, sqrt(3f) / 3, sqrt(3f) / 3))
        assertFloat3Equals(actual = n, expected = Vector(sqrt(3f) / 3, sqrt(3f) / 3, sqrt(3f) / 3))
    }

    @JsName("The_normal_is_a_normalized_vector")
    @Test
    fun `The normal is a normalized vector`() {
        val s = Sphere()
        val n = s.localNormalAt(Point(sqrt(3f) / 3, sqrt(3f) / 3, sqrt(3f) / 3))
        assertFloat3Equals(actual = n, expected = n.normalize())
    }

    @JsName("A_ray_intersects_a_sphere_at_two_points")
    @Test
    fun `A ray intersects a sphere at two points`() {
        val r = Ray(origin = Point(0, 0, -5), direction = Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertEquals(actual = xs.size, expected = 2)
        assertEquals(actual = xs[0].time, expected = 4f)
        assertEquals(actual = xs[1].time, expected = 6f)
    }

    @JsName("A_ray_intersects_a_sphere_at_a_tangent")
    @Test
    fun `A ray intersects a sphere at a tangent`() {
        val r = Ray(origin = Point(0, 1, -5), direction = Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertEquals(actual = xs.size, expected = 2)
        assertEquals(actual = xs[0].time, expected = 5f)
        assertEquals(actual = xs[1].time, expected = 5f)
    }

    @JsName("A_ray_misses_a_sphere")
    @Test
    fun `A ray misses a sphere`() {
        val r = Ray(origin = Point(0, 2, -5), direction = Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertEquals(actual = xs.size, expected = 0)
    }

    @JsName("A_ray_originates_inside_a_sphere")
    @Test
    fun `A ray originates inside a sphere`() {
        val r = Ray(origin = Point(0, 0, 0), direction = Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertEquals(actual = xs.size, expected = 2)
        assertEquals(actual = xs[0].time, expected = 1f)
        assertEquals(actual = xs[1].time, expected = -1f)
    }

    @JsName("A_sphere_is_behind_a_ray")
    @Test
    fun `A sphere is behind a ray`() {
        val r = Ray(origin = Point(0, 0, 5), direction = Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertEquals(actual = xs.size, expected = 2)
        assertEquals(actual = xs[0].time, expected = -6f)
        assertEquals(actual = xs[1].time, expected = -4f)
    }

    @JsName("Intersect_sets_the_object_on_the_intersection")
    @Test
    fun `Intersect sets the object on the intersection`() {
        val r = Ray(origin = Point(0, 0, -5), direction = Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        assertEquals(actual = xs.size, expected = 2)
        assertEquals(actual = xs[0].obj, expected = s)
        assertEquals(actual = xs[1].obj, expected = s)
    }
}
