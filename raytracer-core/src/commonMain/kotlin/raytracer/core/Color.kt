@file:Suppress("NOTHING_TO_INLINE")

package raytracer.core

import raytracer.math.EPSILON
import raytracer.math.ONE_OVER_EPSILON
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

  fun fuzzyEquals(other: Color): Boolean {
    if (abs(red - other.red) > EPSILON) return false
    if (abs(green - other.green) > EPSILON) return false
    if (abs(blue - other.blue) > EPSILON) return false

    return true
  }

  companion object {
    val EMPTY: Color = Color(0f, 0f, 0f)
  }
}

@Suppress("FunctionName")
inline fun Color(
  red: Number = 0,
  green: Number = 0,
  blue: Number = 0
): Color {
  return Color(
    red = red.toFloat(),
    green = green.toFloat(),
    blue = blue.toFloat()
  )
}
