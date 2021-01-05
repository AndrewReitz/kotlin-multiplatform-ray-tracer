@file:Suppress("UNUSED_PARAMETER")

package raytracer.test

import raytracer.math.Float3
import raytracer.math.Float4
import raytracer.math.Matrix
import raytracer.math.fuzzyEquals
import kotlin.test.assertTrue

class UseNamedArguments private constructor()

fun assertFloat4Equals(vararg nothing: UseNamedArguments, actual: Float4, expected: Float4) {
    assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}

fun assertMatrixEquals(vararg nothing: UseNamedArguments, actual: Matrix, expected: Matrix) {
    assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}

fun assertFloatsEquals(vararg nothing: UseNamedArguments, actual: Float, expected: Number, message: String? = null) {
    assertTrue(message ?: "$message Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected.toFloat()) }
}

fun assertFloat3Equals(vararg nothing: UseNamedArguments, actual: Float3, expected: Float3) {
    assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}
