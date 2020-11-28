package raytracer.math

import kotlin.math.pow
import kotlin.math.sqrt

@Suppress("FunctionName")
fun Vector(x: Number, y: Number, z: Number) = Vector3(x.toFloat(), y.toFloat(), z.toFloat())

fun Array<Float>.toVector3(): Vector3 = Vector3(get(0), get(1), get(2))

data class Vector3(
    override val x: Float,
    override val y: Float,
    override val z: Float
) : Float3 {

    operator fun plus(vector: Vector3) =
        Vector3(
            x = x + vector.x,
            y = y + vector.y,
            z = z + vector.z
        )

    operator fun minus(vector: Vector3) =
        Vector3(
            x = x - vector.x,
            y = y - vector.y,
            z = z - vector.z
        )

    operator fun unaryMinus(): Vector3 = Vector3(
        x = -x,
        y = -y,
        z = -z
    )

    operator fun times(scalar: Number): Vector3 {
        val value = scalar.toFloat()
        return Vector3(
            x = value * x,
            y = value * y,
            z = value * z
        )
    }

    operator fun div(scalar: Number): Vector3 {
        val value = scalar.toFloat()
        return Vector3(
            x = x / value,
            y = y / value,
            z = z / value
        )
    }

    val magnitude: Float by lazy(LazyThreadSafetyMode.NONE) {
        sqrt(x.pow(2) + y.pow(2) + z.pow(2))
    }

    fun normalize() = Vector3(
        x / magnitude,
        y / magnitude,
        z / magnitude
    )

    infix fun dot(other: Float3): Float = x * other.x +
        y * other.y +
        z * other.z

    infix fun cross(other: Vector3) = Vector3(
        x = y * other.z - z * other.y,
        y = z * other.x - x * other.z,
        z = x * other.y - y * other.x
    )

    infix fun reflect(normal: Vector3): Vector3 = this - normal * 2 * (this dot normal)
}
