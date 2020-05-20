@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

import kotlin.math.abs

const val EPSILON = 0.0001f
const val ONE_OVER_EPSILON = 1 / EPSILON

inline fun Float.fuzzyEquals(other: Float) = abs(this - other) <= EPSILON

// todo clean all this up and make it make sense
// move `toVector` and `toPoint` helpers to here
// as well on to Float3

interface HasX {
  val x: Float
}

interface HasY {
  val y: Float
}

interface HasZ {
  val z: Float
}

interface HasW {
  val w: Float
}

interface Float3 : HasX, HasY, HasZ {
  fun fuzzyEquals(float3: Float3): Boolean {
    if (abs(x - float3.x) > EPSILON) return false
    if (abs(y - float3.y) > EPSILON) return false
    if (abs(z - float3.z) > EPSILON) return false

    return true
  }
}

interface Float4 : HasX, HasY, HasZ, HasW {
  fun fuzzyEquals(float4: Float4): Boolean
}
