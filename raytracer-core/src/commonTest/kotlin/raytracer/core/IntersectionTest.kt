package raytracer.core

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

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
    val xs = intersectionsOf(i1, i2)
    assertEquals(actual = xs.size, expected = 2)
    assertEquals(actual = xs[0].time, expected = 1f)
    assertEquals(actual = xs[1].time, expected = 2f)
  }
}
