package raytracer.core

import raytracer.math.EPSILON
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntersectionTest {
  @JsName("An_intersection_encapsulates_t_and_object")
  @Test
  fun `An intersection encapsulates t and object`() {
    val s = Sphere()
    val i = Intersection(3.5, s)
    assertEquals(3.5f, i.time)
    assertEquals(s, i.obj)
  }

  @JsName("Aggregating_intersections")
  @Test
  fun `Aggregating intersections`() {
    val s = Sphere()
    val i1 = Intersection(1, s)
    val i2 = Intersection(2, s)
    val xs = Intersections(i1, i2)
    assertEquals(actual = xs.size, expected = 2)
    assertEquals(actual = xs[0].time, expected = 1f)
    assertEquals(actual = xs[1].time, expected = 2f)
  }

  @JsName("Precomputing_the_state_of_an_intersection")
  @Test
  fun `Precomputing the state of an intersection`() {
    val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
    val shape = Sphere()
    val i = Intersection(4, shape)
    val comps = i.prepareComputations(r)
    assertEquals(actual = comps.time, expected = i.time)
    assertEquals(actual = comps.obj, expected = i.obj)
    assertEquals(actual = comps.point, expected = Point(0, 0, -1))
    assertFloat3Equals(actual = comps.eyev, expected = Vector(0, 0, -1))
    assertEquals(actual = comps.normalv, expected = Vector(0, 0, -1))
  }

  @JsName("The_hit_when_an_intersection_occurs_on_the_outside")
  @Test
  fun `The hit, when an intersection occurs on the outside`() {
    val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
    val shape = Sphere()
    val i = Intersection(4, shape)
    val computation = i.prepareComputations(r)
    assertFalse(computation.inside)
  }

  @JsName("The_hit_when_an_intersection_occurs_on_the_inside")
  @Test
  fun `The hit, when an intersection occurs on the inside`() {
    val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
    val shape = Sphere()
    val i = Intersection(1, shape)
    val computation = i.prepareComputations(r)
    assertEquals(actual = computation.point, expected = Point(0, 0, 1))
    assertFloat3Equals(actual = computation.eyev, expected = Vector(0, 0, -1))
    assertFloat3Equals(actual = computation.normalv, expected = Vector(0, 0, -1))
    assertTrue(computation.inside)
  }

  @JsName("The_hit_should_offset_the_point")
  @Test
  fun `The hit should offset the point`() {
    val r = Ray(
      origin = Point(0, 0, -5),
      direction = Vector(0, 0, 1)
    )

    val shape = Sphere(transform = Matrix.translation(0, 0, 1))

    val i = Intersection(5, shape)
    val comps = i.prepareComputations(r)
    assertTrue(comps.overPoint.z < -EPSILON / 2)
    assertTrue(comps.point.z > comps.overPoint.z)
  }
}
