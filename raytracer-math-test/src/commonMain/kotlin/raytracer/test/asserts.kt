package raytracer.test

import raytracer.math.Float3
import raytracer.math.Matrix
import raytracer.math.Vector4
import raytracer.math.fuzzyEquals
import kotlin.test.assertTrue

/** For fuzzy assertions around floating points */
fun assertTupleEquals(actual: Vector4, expected: Vector4) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}

/** For fuzzy assertions around floating points */
fun assertMatrixEquals(actual: Matrix, expected: Matrix) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}

fun assertFloatsEquals(actual: Float, expected: Float) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}

fun assertFloat3Equals(actual: Float3, expected: Float3) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}
