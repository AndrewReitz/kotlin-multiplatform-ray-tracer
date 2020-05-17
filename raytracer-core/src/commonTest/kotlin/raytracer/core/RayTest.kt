package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import raytracer.test.assertTupleEquals

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

  @JsName("A_ray_intersects_a_sphere_at_two_points")
  @Test
  fun `A ray intersects a sphere at two points`() {
    val r = Ray(origin = Point(0, 0, -5), direction = Vector(0, 0, 1))
    val s = Sphere()
    val xs = r.intersects(s)
    assertEquals(actual = xs.size, expected = 2)
    assertEquals(actual = xs[0].time, expected = 4f)
    assertEquals(actual = xs[1].time, expected = 6f)
  }

  @JsName("A_ray_intersects_a_sphere_at_a_tangent")
  @Test
  fun `A ray intersects a sphere at a tangent`() {
    val r = Ray(origin = Point(0, 1, -5), direction = Vector(0, 0, 1))
    val s = Sphere()
    val xs = r.intersects(s)
    assertEquals(actual = xs.size, expected = 2)
    assertEquals(actual = xs[0].time, expected = 5f)
    assertEquals(actual = xs[1].time, expected = 5f)
  }

  @JsName("A_ray_misses_a_sphere")
  @Test
  fun `A ray misses a sphere`() {
    val r = Ray(origin = Point(0, 2, -5), direction = Vector(0, 0, 1))
    val s = Sphere()
    val xs = r.intersects(s)
    assertEquals(actual = xs.size, expected = 0)
  }

  @JsName("A_ray_originates_inside_a_sphere")
  @Test
  fun `A ray originates inside a sphere`() {
    val r = Ray(origin = Point(0, 0, 0), direction = Vector(0, 0, 1))
    val s = Sphere()
    val xs = r.intersects(s)
    assertEquals(actual = xs.size, expected = 2)
    assertEquals(actual = xs[0].time, expected = 1f)
    assertEquals(actual = xs[1].time, expected = -1f)
  }

  @JsName("A_sphere_is_behind_a_ray")
  @Test
  fun `A sphere is behind a ray`() {
    val r = Ray(origin = Point(0, 0, 5), direction = Vector(0, 0, 1))
    val s = Sphere()
    val xs = r.intersects(s)
    assertEquals(actual = xs.size, expected = 2)
    assertEquals(actual = xs[0].time, expected = -6f)
    assertEquals(actual = xs[1].time, expected = -4f)
  }

  @JsName("Intersect_sets_the_object_on_the_intersection")
  @Test
  fun `Intersect sets the object on the intersection`() {
    val r = Ray(origin = Point(0, 0, -5), direction = Vector(0, 0, 1))
    val s = Sphere()
    val xs = r.intersects(s)
    assertEquals(actual = xs.size, expected = 2)
    assertEquals(actual = xs[0].obj, expected = s)
    assertEquals(actual = xs[1].obj, expected = s)
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
