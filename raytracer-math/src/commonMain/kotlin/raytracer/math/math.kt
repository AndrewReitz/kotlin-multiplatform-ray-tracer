package raytracer.math

import kotlin.math.abs
import kotlin.math.sqrt

const val EPSILON = 0.0001f
const val ONE_OVER_EPSILON = 1 / EPSILON

/**
 * Got tired of typing these.
 */
val SQUARE_ROOT_OF_2: Float = sqrt(2.0).toFloat()
val SQUARE_ROOT_OF_2_OVER_2: Float = SQUARE_ROOT_OF_2 / 2f

val Int.isEven get() = this and 1 == 0

fun Float.fuzzyEquals(other: Float) = abs(this - other) <= EPSILON

fun Float3.toVector() = Vector3(x, y, z)
fun Float3.toPoint() = Point(x, y, z)

interface Float3 {
    val x: Float
    val y: Float
    val z: Float

    fun fuzzyEquals(float3: Float3): Boolean {
        if (abs(x - float3.x) > EPSILON) return false
        if (abs(y - float3.y) > EPSILON) return false
        if (abs(z - float3.z) > EPSILON) return false

        return true
    }
}

interface Float4 {
    val x: Float
    val y: Float
    val z: Float
    val w: Float

    fun fuzzyEquals(float4: Float4): Boolean {
        if (abs(x - float4.x) > EPSILON) return false
        if (abs(y - float4.y) > EPSILON) return false
        if (abs(z - float4.z) > EPSILON) return false
        if (abs(w - float4.w) > EPSILON) return false

        return true
    }
}
