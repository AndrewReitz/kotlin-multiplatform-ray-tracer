@file:Suppress("NOTHING_TO_INLINE")

package raytracer.core

import kotlin.math.roundToInt

data class Canvas(
  val width: Int,
  val height: Int
) {

  val pixels: Array<Array<Color>> = Array(height) {
    Array(width) {
      Color.EMPTY
    }
  }

  inline operator fun set(x: Int, y: Int, color: Color) {
    if (x < 0 || x > width) return
    if (y < 0 || y > height) return
    pixels[y][x] = color
  }

  inline operator fun get(x: Int, y: Int) = pixels[y][x]

  fun toPpm(): String {
    val sb = StringBuilder()
      .append("P3").newLine() // ppm type header
      .append("$width $height").newLine()
      .append("255").newLine() // maximum color value

    pixels.forEach { colorRow ->
      val row = MutableList(colorRow.size) { index ->
        val color = colorRow[index]
        val red = (color.red * 255).roundToInt().coerceIn(0, 255)
        val green = (color.green * 255).roundToInt().coerceIn(0, 255)
        val blue = (color.blue * 255).roundToInt().coerceIn(0, 255)
        "$red $green $blue"
      }

      var line = row.joinToString(separator = " ")
      while (line.length > 70) {
        for (index in 70 downTo 67) {
          if (line[index] == ' ') {
            sb.append(line.take(index))
            sb.newLine()
            line = line.substring(index + 1)
            break
          }
        }
      }

      sb.append(line)
      sb.newLine()
    }

    return sb.toString()
  }

  private inline fun StringBuilder.newLine(): StringBuilder = append("\n")
}
