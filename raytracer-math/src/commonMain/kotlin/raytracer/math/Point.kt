@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Suppress("FunctionName")
inline fun Point(x: Number, y: Number, z: Number): Point = Point(x.toFloat(), y.toFloat(), z.toFloat())

inline fun Array<Float>.toPoint(): Point = Point(get(0), get(1), get(2))

inline fun Point.toVector() = Vector3(x, y, z)

data class Point(
  override val x: Float,
  override val y: Float,
  override val z: Float
): Float3 {

  inline operator fun plus(tuple: Float3) = Point(
    x = x + tuple.x,
    y = y + tuple.y,
    z = z + tuple.z
  )

  inline operator fun minus(tuple: Float3) = Point(
    x = x - tuple.x,
    y = y - tuple.y,
    z = z - tuple.z
  )

  inline operator fun not() = Point(
    x = -x,
    y = -y,
    z = -z
  )

  inline operator fun times(scalar: Number): Point {
    val value = scalar.toFloat()
    return Point(
      x = value * x,
      y = value * y,
      z = value * z
    )
  }

  inline operator fun div(scalar: Number): Point {
    val value = scalar.toFloat()
    return Point(
      x = x / value,
      y = y / value,
      z = z / value
    )
  }

  val magnitude: Float by lazy(LazyThreadSafetyMode.NONE) {
    sqrt(x.pow(2) + y.pow(2) + z.pow(2))
  }

  fun normalize() = Point(
    x / magnitude,
    y / magnitude,
    z / magnitude
  )

  inline infix fun dot(other: Point): Float = x * other.x +
      y * other.y +
      z * other.z

  inline infix fun cross(other: Point) = Point(
    x = y * other.z - z * other.y,
    y = z * other.x - x * other.z,
    z = x * other.y - y * other.x
  )

  override fun fuzzyEquals(float3: Float3): Boolean {
    if (abs(x - float3.x) > EPSILON) return false
    if (abs(y - float3.y) > EPSILON) return false
    if (abs(z - float3.z) > EPSILON) return false

    return true
  }
}
