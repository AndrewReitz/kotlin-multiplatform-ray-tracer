package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Matrix4
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertMatrixEquals
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ViewTransformTests {

  @JsName("The_transformation_matrix_for_the_default_orientation")
  @Test
  fun `The transformation matrix for the default orientation`() {
    val from = Point(0, 0, 0)
    val to = Point(0, 0, -1)
    val up = Vector(0, 1, 0)

    val t = viewTransform(from, to, up)
    assertEquals(actual = t, expected = Matrix.IDENTITY)
  }

  @JsName("A_view_transformation_matrix_looking_in_positive_z_direction")
  @Test
  fun `A view transformation matrix looking in positive z direction`() {
    val from = Point(0, 0, 0)
    val to = Point(0, 0, 1)
    val up = Vector(0, 1, 0)
    val t = viewTransform(from, to, up)
    assertEquals(actual = t, expected = Matrix.scaling(-1, 1, -1))
  }

  @JsName("The_view_transformation_moves_the_world")
  @Test
  fun `The view transformation moves the world`() {
    val from = Point(0, 0, 8)
    val to = Point(0, 0, 0)
    val up = Vector(0, 1, 0)

    val t = viewTransform(from, to, up)
    assertEquals(actual = t, expected = Matrix.translation(0, 0, -8))
  }

  @JsName("An_arbitrary_view_transformation")
  @Test
  fun `An arbitrary view transformation`() {
    val from = Point(1, 3, 2)
    val to = Point(4, -2, 8)
    val up = Vector(1, 1, 0)

    val t = viewTransform(from, to, up)
    assertMatrixEquals(
      actual = t,
      expected = Matrix4 {
        r1(-0.50709, 0.50709, 0.67612, -2.36643)
        r2(0.76772, 0.60609, 0.12122, -2.82843)
        r3(-0.35857, 0.59761, -0.71714, 0.00000)
        r4(0.00000, 0.00000, 0.00000, 1.00000)
      }
    )
  }
}
