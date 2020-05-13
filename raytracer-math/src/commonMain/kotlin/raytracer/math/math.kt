@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

import kotlin.math.abs

const val EPSILON = 0.00001
const val ONE_OVER_EPSILON = 1 / EPSILON

inline fun Float.fuzzyEquals(other: Float) = abs(this - other) <= EPSILON
