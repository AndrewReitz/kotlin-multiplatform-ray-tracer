package raytracer.core

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class CanvasTest {
    @JsName("Creating_a_canvas")
    @Test
    fun `Creating a canvas`() {
        val c = Canvas(10, 20)
        assertEquals(actual = c.width, expected = 10)
        assertEquals(actual = c.height, expected = 20)

        (1..c.width).forEach { x ->
            (1..c.height).forEach { y ->
                assertEquals(actual = c[x - 1, y - 1], expected = Color.Black)
            }
        }
    }

    @JsName("Writing_pixels_to_canvas")
    @Test fun `Writing pixels to canvas`() {
        val c = Canvas(10, 20)
        val red = Color(1, 0, 0)
        c[2, 3] = red

        assertEquals(actual = c[2, 3], expected = red)
    }

    @JsName("Constructing_the_PPM_header")
    @Test fun `Constructing the PPM header`() {
        val c = Canvas(5, 3)
        val ppm = c.toPpm()

        val lines = ppm.split("\n").take(3).joinToString(separator = "\n")
        assertEquals(
            actual = lines,
            expected = """
      P3
      5 3
      255
            """.trimIndent()
        )
    }

    @JsName("Constructing_the_PPM_pixel_data")
    @Test fun `Constructing the PPM pixel data`() {
        val canvas = Canvas(5, 3)
        canvas[0, 0] = Color(1.5, 0, 0)
        canvas[2, 1] = Color(0, 0.5, 0)
        canvas[4, 2] = Color(-0.5, 0, 1)
        val ppmLines = canvas.toPpm().split("\n").drop(3).joinToString(separator = "\n")

        assertEquals(
            actual = ppmLines,
            expected = """
      255 0 0 0 0 0 0 0 0 0 0 0 0 0 0
      0 0 0 0 0 0 0 128 0 0 0 0 0 0 0
      0 0 0 0 0 0 0 0 0 0 0 0 0 0 255
      
            """.trimIndent()
        )
    }

    @JsName("Splitting_long_lines_in_PPM_files")
    @Test fun `Splitting long lines in PPM files`() {
        val c = Canvas(10, 2)
        (1..c.width).forEach { x ->
            (1..c.height).forEach { y ->
                c[x - 1, y - 1] = Color(1, 0.8, 0.6)
            }
        }

        val ppm = c.toPpm().split("\n").drop(3).take(4).joinToString(separator = "\n")

        assertEquals(
            actual = ppm,
            expected = """
      255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204
      153 255 204 153 255 204 153 255 204 153 255 204 153
      255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204
      153 255 204 153 255 204 153 255 204 153 255 204 153
            """.trimIndent()
        )
    }

    @JsName("PPM_files_are_terminated_by_a_newline_character")
    @Test fun `PPM files are terminated by a newline character`() {
        val c = Canvas(5, 3)
        val ppm = c.toPpm()
        assertEquals(actual = ppm.last(), expected = '\n')
    }
}
