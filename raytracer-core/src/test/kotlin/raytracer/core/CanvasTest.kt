package raytracer.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CanvasTest {
  @Test fun `Creating a canvas`() {
    val c = Canvas(10, 20)
    assertEquals(actual = c.width, expected = 10)
    assertEquals(actual = c.height, expected = 20)

    var count = 0
    c.pixels.forEach {
      it.forEach { color ->
        count++
        assertEquals(actual = color, expected = Color.EMPTY)
      }
    }

    assertEquals(actual = count, expected = 200)
  }

  @Test fun `Writing pixels to canvas`()  {
    val c = Canvas(10, 20)
    val red = Color(1, 0, 0)
    c[2, 3] = red

    assertEquals(actual = c[2, 3], expected = red)
  }
}
