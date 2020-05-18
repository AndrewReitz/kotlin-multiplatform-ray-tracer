@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

import kotlin.math.abs

const val EPSILON = 0.00001
const val ONE_OVER_EPSILON = 1 / EPSILON

inline fun Float.fuzzyEquals(other: Float) = abs(this - other) <= EPSILON

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
  fun fuzzyEquals(float3: Float3): Boolean
}

interface Float4 : HasX, HasY, HasZ, HasW {
  fun fuzzyEquals(float4: Float4): Boolean
}
