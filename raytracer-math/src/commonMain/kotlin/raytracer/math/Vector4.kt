@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class Vector4(
  override val x: Float,
  override val y: Float,
  override val z: Float,
  override val w: Float
): Float4 {

  val isPoint: Boolean = w == 1.0F
  val isVector: Boolean = w == 0.0F

  inline operator fun plus(vector4: Vector4) = Vector4(
    x = x + vector4.x,
    y = y + vector4.y,
    z = z + vector4.z,
    w = w + vector4.w
  )

  inline operator fun minus(vector4: Vector4) = Vector4(
    x = x - vector4.x,
    y = y - vector4.y,
    z = z - vector4.z,
    w = w - vector4.w
  )

  inline operator fun not() = Vector4(
    x = -x,
    y = -y,
    z = -z,
    w = -w
  )

  inline operator fun times(scalar: Number): Vector4 {
    val value = scalar.toFloat()
    return Vector4(
      x = value * x,
      y = value * y,
      z = value * z,
      w = value * w
    )
  }

  inline operator fun div(scalar: Number): Vector4 {
    val value = scalar.toFloat()
    return Vector4(
      x = x / value,
      y = y / value,
      z = z / value,
      w = w / value
    )
  }

  val magnitude: Float by lazy(LazyThreadSafetyMode.NONE) {
    sqrt(x.pow(2) + y.pow(2) + z.pow(2) + w.pow(2))
  }

  fun normalize() = Vector4(
      x / magnitude,
      y / magnitude,
      z / magnitude,
      w / magnitude
    )

  inline infix fun dot(other: Point): Float = x * other.x +
      y * other.y +
      z * other.z +
      w * 1f

  inline infix fun dot(other: Vector3): Float = x * other.x +
      y * other.y +
      z * other.z +
      w * 0f

  inline infix fun dot(other: Vector4): Float = x * other.x +
      y * other.y +
      z * other.z +
      w * other.w

  inline infix fun cross(other: Vector4) = Vector4(
    x = y * other.z - z * other.y,
    y = z * other.x - x * other.z,
    z = x * other.y - y * other.x,
    w = w
  )

  override fun fuzzyEquals(float4: Float4): Boolean {
    if (abs(x - float4.x) > EPSILON) return false
    if (abs(y - float4.y) > EPSILON) return false
    if (abs(z - float4.z) > EPSILON) return false
    if (abs(w - float4.w) > EPSILON) return false

    return true
  }
}

@Suppress("FunctionName")
inline fun Vector4(x: Number, y: Number, z: Number, w: Number) = Vector4(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

inline fun Array<Float>.toVector4(): Vector4 = Vector4(get(0), get(1), get(2), get(3))
