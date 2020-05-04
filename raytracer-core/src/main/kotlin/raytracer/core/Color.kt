@file:Suppress("NOTHING_TO_INLINE")

package raytracer.core

import raytracer.math.EPSILON
import kotlin.math.abs

data class Color(
  val red: Float,
  val green: Float,
  val blue: Float
) {

  inline operator fun plus(color: Color) = Color(
    red = red + color.red,
    blue = blue + color.blue,
    green = green + color.green
  )

  inline operator fun minus(color: Color) = Color(
    red = red - color.red,
    blue = blue - color.blue,
    green = green - color.green
  )

  inline operator fun times(scalar: Number): Color {
    val value = scalar.toFloat()
    return Color(
      red = red * value,
      blue = blue * value,
      green = green * value
    )
  }

  inline operator fun times(color: Color) = Color(
    red = red * color.red,
    blue = blue * color.blue,
    green = green * color.green
  )

  // mostly for testing to over come floating point math issues
  fun mostlyEqual(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Color

    if (abs(red - other.red) > EPSILON) return false
    if (abs(green - other.green) > EPSILON) return false
    if (abs(blue - other.blue) > EPSILON) return false

    return true
  }

  companion object {
    val EMPTY = Color(0, 0, 0)
  }
}

@Suppress("FunctionName")
inline fun Color(
  red: Number = 0,
  green: Number = 0,
  blue: Number = 0
): Color {
  if (red == 0 && green == 0 && blue == 0) {
    return Color.EMPTY
  }

  return Color(
    red = red.toFloat(),
    green = green.toFloat(),
    blue = blue.toFloat()
  )
}
