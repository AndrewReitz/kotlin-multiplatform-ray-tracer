package raytracer.core

import org.junit.jupiter.api.Test
import raytracer.math.EPSILON
import kotlin.test.assertEquals

class ColorTest {
  @Test fun `Colors are (red, green, blue) tuples`() {
    val c = Color(-0.5, 0.4, 1.7)

    assertEquals(actual = c.red, expected = -0.5F)
    assertEquals(actual = c.green, expected = 0.4F)
    assertEquals(actual = c.blue, expected = 1.7F)
  }

  @Test fun `Adding colors`() {
    val c1 = Color(0.9, 0.6, 0.75)
    val c2 = Color(0.7, 0.1, 0.25)

    val result = c1 + c2
    assertEquals(actual = result, expected = Color(1.6, 0.7, 1.0))
    assertEquals(actual = result.hashCode(), expected = Color(1.6, 0.7, 1.0).hashCode())
  }

  @Test fun `Subtracting colors`() {
    val c1 = Color(0.9, 0.6, 0.75)
    val c2 = Color(0.7, 0.1, 0.25)

    val result = c1 - c2
    assertEquals(actual = result, expected = Color(0.2, 0.5, 0.5))
  }

  @Test fun `Multiplying a color by a scalar`() {
    val c = Color(0.2, 0.3, 0.4)
    assertEquals(actual = c * 2, expected = Color(0.4, 0.6, 0.8))
  }

  @Test fun `Multiplying colors`() {
    val c1 = Color(1, 0.2, 0.4)
    val c2 = Color(0.9, 1, 0.1)

    val result = c1 * c2
    assertEquals(actual = result, expected = Color(0.9, 0.2, 0.04))
    assertEquals(actual = result.hashCode(), expected = Color(0.9, 0.2, 0.04).hashCode())
  }

  @Test fun `Make sure equals and hashcode work correctly`() {
    val c1 = Color(0, 0, 0.5)
    val c2 = Color(0, 0, 0.4999999)
    assertEquals(c1, c2)
    assertEquals(c1.hashCode(), c2.hashCode())

    val c3 = Color(0, 0, 0.5 - EPSILON)
    val c4 = Color(0, 0, 0.4999999 - EPSILON)
    assertEquals(c3, c4)
    assertEquals(c3.hashCode(), c4.hashCode())
  }
}
