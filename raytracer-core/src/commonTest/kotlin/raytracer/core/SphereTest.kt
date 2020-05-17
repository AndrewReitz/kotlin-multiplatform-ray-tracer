package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class SphereTest {
  @JsName("A_sphere's_default_transformation")
  @Test
  fun `A sphere's default transformation`() {
    val s = Sphere()
    assertEquals(actual = s.transform, expected = Matrix.IDENTITY)
  }

  @JsName("Changing_a_sphere's_transformation")
  @Test
  fun `Changing a sphere's transformation`() {
    val t = Matrix.translation(2, 3, 4)
    val s = Sphere(transform = t)
    assertEquals(actual = s.transform, expected = t)
  }

  @JsName("Intersecting_a_scaled_sphere_with_a_ray")
  @Test
  fun `Intersecting a scaled sphere with a ray`() {
    val r = Ray(Point(0, 0, -5), Vector(0, 0 ,1))
    val s = Sphere(transform = Matrix.scaling(2, 2, 2))
    val xs = r.intersects(s)
    assertEquals(actual = xs.size, expected = 2)
    assertEquals(actual = xs[0].time, expected = 3f)
    assertEquals(actual = xs[1].time, expected = 7f)
  }

  @JsName("Intersecting_a_translated_sphere_with_a_ray")
  @Test
  fun `Intersecting a translated sphere with a ray`() {
    val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
    val s = Sphere(transform = Matrix.translation(5, 0, 0))
    val xs = r.intersects(s)
    assertEquals(actual = xs.size, expected = 0)
  }
}
