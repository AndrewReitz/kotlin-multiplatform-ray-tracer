package raytracer.core

import raytracer.math.EPSILON
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColorTest {
  @JsName("Colors_are_red_green_blue_tuples")
  @Test
  fun `Colors are (red, green, blue) tuples`() {
    val c = Color(-0.5, 0.4, 1.7)

    assertEquals(actual = c.red, expected = -0.5F)
    assertEquals(actual = c.green, expected = 0.4F)
    assertEquals(actual = c.blue, expected = 1.7F)
  }

  @JsName("Adding_colors")
  @Test fun `Adding colors`() {
    val c1 = Color(0.9, 0.6, 0.75)
    val c2 = Color(0.7, 0.1, 0.25)

    val result = c1 + c2
    assertColorEquals(actual = result, expected = Color(1.6, 0.7, 1.0))
  }

  @JsName("Subtracting_colors")
  @Test fun `Subtracting colors`() {
    val c1 = Color(0.9, 0.6, 0.75)
    val c2 = Color(0.7, 0.1, 0.25)

    val result = c1 - c2
    assertColorEquals(actual = result, expected = Color(0.2, 0.5, 0.5))
  }

  @JsName("Multiplying_a_color_by_a_scalar")
  @Test fun `Multiplying a color by a scalar`() {
    val c = Color(0.2, 0.3, 0.4)
    assertEquals(actual = c * 2, expected = Color(0.4, 0.6, 0.8))
  }

  @JsName("Multiplying_colors")
  @Test fun `Multiplying colors`() {
    val c1 = Color(1, 0.2, 0.4)
    val c2 = Color(0.9, 1, 0.1)

    val result = c1 * c2
    assertColorEquals(actual = result, expected = Color(0.9, 0.2, 0.04))
  }

  @JsName("Color_make_sure_equals_and_hashcode_work_correctly")
  @Test fun `Color make sure equals and hashcode work correctly`() {
    val c1 = Color(0, 0, 0.5)
    val c2 = Color(0, 0, 0.4999999)
    assertColorEquals(c1, c2)

    val c3 = Color(0, 0, 0.5 - EPSILON)
    val c4 = Color(0, 0, 0.4999999 - EPSILON)
    assertColorEquals(c3, c4)
  }

  /** For fuzzy assertions around floating points */
  private fun assertColorEquals(expected: Color, actual: Color) {
    assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
  }
}
