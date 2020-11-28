package raytracer.math

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector4(
    override val x: Float,
    override val y: Float,
    override val z: Float,
    override val w: Float
) : Float4 {

    val isPoint: Boolean = w == 1.0F
    val isVector: Boolean = w == 0.0F

    operator fun plus(vector4: Vector4) = Vector4(
        x = x + vector4.x,
        y = y + vector4.y,
        z = z + vector4.z,
        w = w + vector4.w
    )

    operator fun minus(vector4: Vector4) = Vector4(
        x = x - vector4.x,
        y = y - vector4.y,
        z = z - vector4.z,
        w = w - vector4.w
    )

    operator fun not() = Vector4(
        x = -x,
        y = -y,
        z = -z,
        w = -w
    )

    operator fun times(scalar: Number): Vector4 {
        val value = scalar.toFloat()
        return Vector4(
            x = value * x,
            y = value * y,
            z = value * z,
            w = value * w
        )
    }

    operator fun div(scalar: Number): Vector4 {
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

    infix fun dot(other: Point): Float = x * other.x +
        y * other.y +
        z * other.z +
        w * 1f

    infix fun dot(other: Vector3): Float = x * other.x +
        y * other.y +
        z * other.z +
        w * 0f

    infix fun dot(other: Vector4): Float = x * other.x +
        y * other.y +
        z * other.z +
        w * other.w

    infix fun cross(other: Vector4) = Vector4(
        x = y * other.z - z * other.y,
        y = z * other.x - x * other.z,
        z = x * other.y - y * other.x,
        w = w
    )
}

@Suppress("FunctionName")
fun Vector4(x: Number, y: Number, z: Number, w: Number) = Vector4(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

fun Array<Float>.toVector4(): Vector4 = Vector4(get(0), get(1), get(2), get(3))
