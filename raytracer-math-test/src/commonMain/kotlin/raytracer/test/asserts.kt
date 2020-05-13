package raytracer.test

import raytracer.math.Matrix
import raytracer.math.Tuple
import raytracer.math.fuzzyEquals
import kotlin.test.assertTrue

/** For fuzzy assertions around floating points */
fun assertTupleEquals(actual: Tuple, expected: Tuple) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}

/** For fuzzy assertions around floating points */
fun assertMatrixEquals(actual: Matrix, expected: Matrix) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}

fun assertFloatsEquals(actual: Float, expected: Float) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}
