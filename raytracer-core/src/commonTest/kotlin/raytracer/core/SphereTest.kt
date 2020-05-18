package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class SphereTest {
  @JsName("A_spheres_default_transformation")
  @Test
  fun `A sphere's default transformation`() {
    val s = Sphere()
    assertEquals(actual = s.transform, expected = Matrix.IDENTITY)
  }

  @JsName("Changing_a_spheres_transformation")
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

  @JsName("The_normal_on_a_sphere_at_a_point_on_the_x_axis")
  @Test
  fun `The normal on a sphere at a point on the x axis`() {
    val s = Sphere()
    val n = s.normalAt(Point(1, 0, 0))
    assertEquals(actual = n, expected = Vector(1, 0, 0))
  }

  @JsName("The_normal_on_a_sphere_at_a_point_on_the_y_axis")
  @Test
  fun `The normal on a sphere at a point on the y axis`() {
    val s = Sphere()
    val n = s.normalAt(Point(0, 1, 0))
    assertEquals(actual = n, expected = Vector(0, 1, 0))
  }

  @JsName("The_normal_on_a_sphere_at_a_point_on_the_z_axis")
  @Test
  fun `The normal on a sphere at a point on the z axis`() {
    val s = Sphere()
    val n = s.normalAt(Point(0, 0, 1))
    assertEquals(actual = n, expected = Vector(0, 0, 1))
  }

  @JsName("The_normal_on_a_sphere_at_a_nonaxial_point")
  @Test
  fun `The normal on a sphere at a nonaxial point`() {
    val s = Sphere()
    val n = s.normalAt(Point(sqrt(3f) / 3, sqrt(3f) / 3, sqrt(3f) / 3))
    assertFloat3Equals(actual = n, expected = Vector(sqrt(3f) / 3, sqrt(3f) / 3, sqrt(3f) / 3))
  }

  @JsName("The_normal_is_a_normalized_vector")
  @Test
  fun `The normal is a normalized vector`() {
    val s = Sphere()
    val n = s.normalAt(Point(sqrt(3f) / 3, sqrt(3f) / 3, sqrt(3f) / 3))
    assertFloat3Equals(actual = n, expected = n.normalize())
  }

  @JsName("Computing_the_normal_on_a_translated_sphere")
  @Test
  fun `Computing the normal on a translated sphere`() {
    val s = Sphere(transform = Matrix.translation(0, 1, 0))
    val n = s.normalAt(Point(0, 1.70711, -0.70711))
    assertFloat3Equals(actual = n, expected = Vector(0, 0.70711, -0.70711))
  }

  @JsName("Computing_the_normal_on_a_transformed_sphere")
  @Test
  fun `Computing the normal on a transformed sphere`() {
    val m = Matrix.scaling(1, 0.5, 1) * Matrix.rotationZ(PI / 5)
    val s = Sphere(transform = m)
    val n = s.normalAt(Point(0, sqrt(2f) / 2, -sqrt(2f) / 2))
    assertFloat3Equals(actual = n, expected = Vector(0, 0.97014, -0.24254))
  }
}
