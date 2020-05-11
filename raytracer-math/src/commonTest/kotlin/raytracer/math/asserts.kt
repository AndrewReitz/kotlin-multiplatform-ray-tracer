package raytracer.math

import kotlin.test.assertTrue

/** For fuzzy assertions around floating points */
fun assertTupleEquals(actual: Tuple, expected: Tuple) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}

/** For fuzzy assertions around floating points */
fun assertMatrixEquals(actual: Matrix, expected: Matrix) {
  assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
}
