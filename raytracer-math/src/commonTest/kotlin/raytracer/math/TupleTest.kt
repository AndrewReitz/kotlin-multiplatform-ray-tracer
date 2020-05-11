package raytracer.math

import kotlin.js.JsName
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TupleTest {

  @JsName("A_tuple_with_w1_0_is_a_point")
  @Test
  fun `A tuple with w=1,0 is a point`() {
    val a = Tuple(4.3, -4.2, 3.1, 1.0)

    assertEquals(actual = a.x, expected = 4.3F)
    assertEquals(actual = a.y, expected = -4.2F)
    assertEquals(actual = a.z, expected = 3.1F)
    assertEquals(actual = a.w, expected = 1.0F)

    assertTrue(a.isPoint)
    assertFalse(a.isVector)
  }

  @JsName("A_tuple_with_w0_is_a_vector")
  @Test
  fun `A tuple with w=0 is a vector`() {
    val a = Tuple(4.3, -4.2, 3.1, 0.0)

    assertEquals(actual = a.x, expected = 4.3F)
    assertEquals(actual = a.y, expected = -4.2F)
    assertEquals(actual = a.z, expected = 3.1F)
    assertEquals(actual = a.w, expected = 0.0F)

    assertFalse(a.isPoint)
    assertTrue(a.isVector)
  }

  @JsName("Point_creates_tuples_with_w_1")
  @Test
  fun `Point() creates tuples with w=1`() {
    val p = Point(4, -4, 3)
    assertEquals(actual = p, expected = Tuple(4, -4, 3, 1))
  }

  @JsName("Vector_creates_tuples_with_w0")
  @Test
  fun `Vector() creates tuples with w=0`() {
    val v = Vector(4, -4, 3)
    assertEquals(actual = v, expected = Tuple(4, -4, 3, 0))
  }

  @JsName("Adding_two_tuples")
  @Test
  fun `Adding two tuples`() {
    val a1 = Tuple(3, -2, 5, 1)
    val a2 = Tuple(-2, 3, 1, 0)
    val result = a1 + a2

    assertEquals(actual = result, expected = Tuple(1, 1, 6, 1))
  }

  @JsName("Subtracting_two_points")
  @Test
  fun `Subtracting two points`() {
    val p1 = Point(3, 2, 1)
    val p2 = Point(5, 6, 7)

    val result = p1 - p2

    assertEquals(actual = result, expected = Vector(-2, -4, -6))
  }

  @JsName("Subtracting_a_vector_from_a_point")
  @Test
  fun `Subtracting a vector from a point`() {
    val p = Point(3, 2, 1)
    val v = Vector(5, 6, 7)

    val result = p - v
    assertEquals(actual = result, expected = Point(-2, -4, -6))
  }

  @JsName("Subtracting_two_vectors")
  @Test
  fun `Subtracting two vectors`() {
    val v1 = Vector(3, 2, 1)
    val v2 = Vector(5, 6, 7)

    val result = v1 - v2
    assertEquals(actual = result, expected = Vector(-2, -4, -6))
  }

  @JsName("Subtracting_a_vector_from_a_zero_vector")
  @Test
  fun `Subtracting a vector from a zero vector`() {
    val zero = Vector(0, 0, 0)
    val v = Vector(1, -2, 3)

    val result = zero - v
    assertEquals(actual = result, expected = Vector(-1, 2, -3))
  }

  @JsName("Negating_a_tuple")
  @Test
  fun `Negating a tuple`() {
    val a = Tuple(1, -2, 3, -4)
    assertEquals(actual = !a, expected = Tuple(-1, 2, -3, 4))
  }

  @JsName("Multiplying_a_tuple_by_a_scalar")
  @Test
  fun `Multiplying a tuple by a scalar`() {
    val a = Tuple(1, -2, 3, -4)
    val result = a * 3.5

    assertEquals(actual = result, expected = Tuple(3.5, -7, 10.5, -14))
  }

  @JsName("Multiplying_a_tuple_by_a_fraction")
  @Test
  fun `Multiplying a tuple by a fraction`() {
    val a = Tuple(1, -2, 3, -4)
    val result = a * 0.5

    assertEquals(actual = result, expected = Tuple(0.5, -1, 1.5, -2))
  }

  @JsName("Dividing_a_tuple_by_a_scalar")
  @Test
  fun `Dividing a tuple by a scalar`() {
    val a = Tuple(1, -2, 3, -4)
    val result = a / 2

    assertEquals(actual = result, expected = Tuple(0.5, -1, 1.5, -2))
  }

  @JsName("Computing_the_magnitude_of_vector_1_0_0")
  @Test
  fun `Computing the magnitude of vector(1, 0, 0)`() {
    val v = Vector(1, 0, 0)
    assertEquals(actual = v.magnitude, expected = 1.toFloat())
  }

  @JsName("Computing_the_magnitude_of_vector0_1_0")
  @Test
  fun `Computing the magnitude of vector(0, 1, 0)`() {
    val v = Vector(0, 1, 0)
    assertEquals(actual = v.magnitude, expected = 1.toFloat())
  }

  @JsName("Computing_the_magnitude_of_vector0_0_1")
  @Test
  fun `Computing the magnitude of vector(0, 0, 1)`() {
    val v = Vector(0, 0, 1)
    assertEquals(actual = v.magnitude, expected = 1.toFloat())
  }

  @JsName("Computing_the_magnitude_of_vector1_2_3")
  @Test
  fun `Computing the magnitude of vector(1, 2, 3)`() {
    val v = Vector(1, 2, 3)
    assertEquals(actual = v.magnitude, expected = sqrt(14F))
  }

  @JsName("Computing_the_magnitude_of_vectorNeg1Neg2Neg3")
  @Test
  fun `Computing the magnitude of vector(-1, -2, -3)`() {
    val v = Vector(-1, -2, -3)
    assertEquals(actual = v.magnitude, expected = sqrt(14F))
  }

  @JsName("Normalizing_vector4_0_0_gives_1_0_0")
  @Test
  fun `Normalizing vector(4, 0, 0) gives (1, 0, 0)`() {
    val v = Vector(4, 0, 0)
    assertEquals(actual = v.normalize(), expected = Vector(1, 0, 0))
  }

  @JsName("Normalizing_vector1_2_3_gives_0_26726_0_53452_0_80178")
  @Test
  fun `Normalizing vector(1, 2, 3) gives (0,26726, 0,53452, 0,80178)`() {
    val v = Vector(1, 2, 3)
    assertTupleEquals(actual = v.normalize(), expected = Vector(0.26726, 0.53452, 0.80178))
  }

  @JsName("The_magnitude_of_a_normalized_vector")
  @Test
  fun `The magnitude of a normalized vector`() {
    val v = Vector(1, 2, 3).normalize()
    assertTrue { v.magnitude - 1F < EPSILON }
  }

  @JsName("The_dot_product_of_two_tuples")
  @Test
  fun `The dot product of two tuples`() {
    val a = Vector(1, 2, 3)
    val b = Vector(2, 3, 4)

    assertEquals(actual = a dot b, expected = 20F)
  }

  @JsName("The_cross_product_of_two_vectors")
  @Test
  fun `The cross product of two vectors`() {
    val a = Vector(1, 2, 3)
    val b = Vector(2, 3, 4)

    assertEquals(actual = a cross b, expected = Vector(-1, 2, -1))
    assertEquals(actual = b cross a, expected = Vector(1, -2, 1))
  }

  /** For fuzzy assertions around floating points */
  private fun assertTupleEquals(actual: Tuple, expected: Tuple) {
    assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
  }
}
