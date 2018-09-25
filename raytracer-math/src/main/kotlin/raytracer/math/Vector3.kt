package raytracer.math

import java.lang.IllegalStateException

@Suppress("NonAsciiCharacters", "NOTHING_TO_INLINE")
data class Vector3(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0
) {

    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun plus(vector: Vector3) = Vector3(
            x = x + vector.x,
            y = y + vector.y,
            z = z + vector.z
    )

    operator fun minus(vector: Vector3) = Vector3(
            x = x - vector.x,
            y = y - vector.y,
            z = z - vector.z
    )

    operator fun times(value: Double) = Vector3(
            x = value * x,
            y = value * y,
            z = value * z
    )

    operator fun div(value: Double): Vector3 {
        if (value == 0.0) throw IllegalStateException("Cannot divide by 0")
        return Vector3(
                x = x / value,
                y = y / value,
                z = z / value
        )
    }

    operator fun not() = Vector3(
            x = -x,
            y = -y,
            z = -z
    )

    infix fun dotProduct(vector: Vector3): Double = x * vector.x + y * vector.y + z * vector.z
    infix fun `∙`(vector: Vector3) = dotProduct(vector)

    inline infix fun crossProduct(vector: Vector3) = Vector3(
            x = (y * vector.z) - (z * vector.y),
            y = (z * vector.x) - (x * vector.z),
            z = (x * vector.y) - (y * vector.x)
    )
    inline infix fun `×`(vector: Vector3) = crossProduct(vector)

    val normalize: Vector3 by lazy { this / this.length }
    val length by lazy { Math.sqrt(lengthSquared) }

    private val lengthSquared: Double by lazy { this dotProduct this }
}