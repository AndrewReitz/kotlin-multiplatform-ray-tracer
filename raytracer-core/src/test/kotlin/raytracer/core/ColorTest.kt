package raytracer.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    assertTrue {
      result.mostlyEqual(Color(1.6, 0.7, 1.0))
    }
  }

  @Test fun `Subtracting colors`() {
    val c1 = Color(0.9, 0.6, 0.75)
    val c2 = Color(0.7, 0.1, 0.25)

    val result = c1 - c2
    assertTrue { result.mostlyEqual(Color(0.2, 0.5, 0.5)) }
  }

  @Test fun `Multiplying a color by a scalar`() {
    val c = Color(0.2, 0.3, 0.4)
    assertEquals(actual = c * 2, expected = Color(0.4, 0.6, 0.8))
  }

  @Test fun `Multiplying colors`() {
    val c1 = Color(1, 0.2, 0.4)
    val c2 = Color(0.9, 1, 0.1)

    val result = c1 * c2
    assertTrue { result.mostlyEqual(Color(0.9, 0.2, 0.04)) }
  }
}
