package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.math.Vector3
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class WorldTest {

  @JsName("Creating_a_world")
  @Test
  fun `Creating a world`() {
    val w = World()
    assertEquals(actual = w.lights, expected = emptyList())
    assertEquals(actual = w.objects, expected = emptyList())
  }

  @JsName("The_default_world")
  @Test
  fun `The default world`() {
    val light = PointLight(
      position = Point(-10, -10, -10),
      intensity = Color(1, 1, 1)
    )

    val s1 = Sphere(
      material = Material(
        color = Color(
          red = 0.8,
          green = 1.0,
          blue = 0.6
        ),
        diffuse = 0.7f,
        specular = 0.2f
      )
    )

    val s2 = Sphere(
      transform = Matrix.scaling(0.5, 0.5, 0.5)
    )

    val w = World.default
    assertEquals(actual = w.lights, expected = listOf(light))
    assertEquals(actual = w.objects, expected = listOf(s1, s2))
  }

  @JsName("Intersect_a_world_with_a_ray")
  @Test
  fun `Intersect a world with a ray`() {
    val w = World.default
    val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
    val xs = w.intersect(r)
    assertEquals(actual = xs.size, expected = 4)
    assertEquals(actual = xs[0].time, expected = 4f)
    assertEquals(actual = xs[1].time, expected = 4.5f)
    assertEquals(actual = xs[2].time, expected = 5.5f)
    assertEquals(actual = xs[3].time, expected = 6f)
  }

  @JsName("Shading_an_intersection")
  @Test
  fun `Shading an intersection`() {
    val w = World.default
    val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
    val shape = w.objects.first()
    val i = Intersection(4, shape)
    val comps = i.prepareComputations(r)
    val c = w.shadeHit(comps)
    assertFloat3Equals(actual = c, expected = Color(0.38066, 0.47583, 0.2855))
  }

  @JsName("Shading_an_intersection_from_the_inside")
  @Test
  fun `Shading an intersection from the inside`() {
    val w = World.default.copy(
      lights = listOf(
        PointLight(
          position = Point(0, 0.25, 0),
          intensity = Color.White
        )
      )
    )

    val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
    val shape = w.objects.take(2).last()
    val i = Intersection(0.5, shape)
    val comps = i.prepareComputations(r)
    val c = w.shadeHit(comps)
    assertFloat3Equals(actual = c, expected = Color(0.90498, 0.90498, 0.90498))
  }

  @JsName("The_color_when_a_ray_misses")
  @Test
  fun `The color when a ray misses`() {
    val w = World.default
    val r = Ray(Point(0, 0, -5), Vector(0, 1, 0))
    val c = w.colorAt(r)
    assertFloat3Equals(actual = c, expected = Color.Black)
  }

  @JsName("The_color_with_an_intersection_behind_the_ray")
  @Test
  fun `The color with an intersection behind the ray`() {
    val default = World.default
    val outer = default.objects.first()
    val inner = default.objects.last()

    val w = World.default.copy(
      objects = listOf(
        outer.copy(
          material = outer.material.copy(ambient = 1f)
        ),
        inner.copy(
          material = inner.material.copy(ambient = 1f)
        )
      )
    )

    val r = Ray(Point(0, 0, 0.75), Vector(0, 0, -1))
    val c = w.colorAt(r)
    assertFloat3Equals(actual = c, expected = inner.material.color)
  }
}