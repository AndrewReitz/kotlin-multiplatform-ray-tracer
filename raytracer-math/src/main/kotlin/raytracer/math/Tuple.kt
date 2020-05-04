@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Tuple(
  val x: Float,
  val y: Float,
  val z: Float,
  val w: Float
) {

  val isPoint: Boolean = w == 1.0F
  val isVector: Boolean = w == 0.0F

  inline operator fun plus(tuple: Tuple) = Tuple(
    x = x + tuple.x,
    y = y + tuple.y,
    z = z + tuple.z,
    w = w + tuple.w
  )

  inline operator fun minus(tuple: Tuple) = Tuple(
    x = x - tuple.x,
    y = y - tuple.y,
    z = z - tuple.z,
    w = w - tuple.w
  )

  inline operator fun not() = Tuple(
    x = -x,
    y = -y,
    z = -z,
    w = -w
  )

  inline operator fun times(scalar: Number): Tuple {
    val value = scalar.toFloat()
    return Tuple(
      x = value * x,
      y = value * y,
      z = value * z,
      w = value * w
    )
  }

  inline operator fun div(scalar: Number): Tuple {
    val value = scalar.toFloat()
    return Tuple(
      x = x / value,
      y = y / value,
      z = z / value,
      w = w / value
    )
  }

  val magnitude: Float by lazy {
    sqrt(x.pow(2) + y.pow(2) + z.pow(2) + w.pow(2))
  }

  fun normalize() = Tuple(
      x / magnitude,
      y / magnitude,
      z / magnitude,
      w / magnitude
    )

  inline infix fun dot(other: Tuple): Float = x * other.x +
      y * other.y +
      z * other.z +
      w * other.w

  // Vectors only, no need for w
  inline infix fun cross(other: Tuple) = Tuple(
    x = y * other.z - z * other.y,
    y = z * other.x - x * other.z,
    z = x * other.y - y * other.x,
    w = w
  )

  // mostly for test, but fixes issues with float point math
  fun mostlyEqual(other: Tuple): Boolean {
    if (abs(x - other.x) > EPSILON) return false
    if (abs(y - other.y) > EPSILON) return false
    if (abs(z - other.z) > EPSILON) return false
    if (abs(w - other.w) > EPSILON) return false

    return true
  }
}

@Suppress("FunctionName")
inline fun Tuple(x: Number, y: Number, z: Number, w: Number) = Tuple(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

@Suppress("FunctionName")
inline fun Point(x: Number, y: Number, z: Number) = Tuple(x, y, z, 1)

@Suppress("FunctionName")
inline fun Vector(x: Number, y: Number, z: Number) = Tuple(x, y, z, 0)
