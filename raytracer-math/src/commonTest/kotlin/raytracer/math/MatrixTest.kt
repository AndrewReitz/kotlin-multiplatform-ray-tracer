@file:Suppress("LocalVariableName")

package raytracer.math

import raytracer.math.Matrix.Companion.IDENTITY
import kotlin.js.JsName
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MatrixTest {
  @JsName("Constructing_and_inspecting_a_4x4_matrix")
  @Test
  fun `Constructing and inspecting a 4x4 matrix`() {
    val m = Matrix {
      row(1, 2, 3, 4)
      row(5.5, 6.5, 7.5, 8.5)
      row(9, 10, 11, 12)
      row(13.5, 14.5, 15.5, 16.5)
    }

    assertEquals(actual = m[0, 0], expected = 1f)
    assertEquals(actual = m[0, 3], expected = 4f)
    assertEquals(actual = m[1, 0], expected = 5.5f)
    assertEquals(actual = m[1, 2], expected = 7.5f)
    assertEquals(actual = m[2, 2], expected = 11f)
    assertEquals(actual = m[3, 0], expected = 13.5f)
    assertEquals(actual = m[3, 2], expected = 15.5f)
  }

  @JsName("A_2x2_matrix_ought_to_be_representable")
  @Test
  fun `A 2x2 matrix ought to be representable`() {
    val m = Matrix {
      row(-3, 5)
      row(1, -2)
    }

    assertEquals(actual = m[0, 0], expected = -3f)
    assertEquals(actual = m[0, 1], expected = 5f)
    assertEquals(actual = m[1, 0], expected = 1f)
    assertEquals(actual = m[1, 1], expected = -2f)
  }

  @JsName("Matrix_equality_with_identical_matrices")
  @Test
  fun `Matrix equality with identical matrices`() {
    val a = Matrix {
      row(1, 2, 3, 4)
      row(5, 6, 7, 8)
      row(9, 8, 7, 6)
      row(5, 4, 3, 2)
    }

    val b = Matrix {
      row(1, 2, 3, 4)
      row(5, 6, 7, 8)
      row(9, 8, 7, 6)
      row(5, 4, 3, 2)
    }

    assertEquals(a, b)
  }

  @JsName("Matrix_equality_with_different_matrices")
  @Test
  fun `Matrix equality with different matrices`() {
    val a = Matrix {
      row(1, 2, 3, 4)
      row(5, 6, 7, 8)
      row(9, 8, 7, 6)
      row(5, 4, 3, 2)
    }

    val b = Matrix {
      row(2, 3, 4, 5)
      row(6, 7, 8, 9)
      row(8, 7, 6, 5)
      row(4, 3, 2, 1)
    }

    assertNotEquals(a, b)
  }

  @JsName("Multiplying_two_matrices")
  @Test
  fun `Multiplying two matrices`() {
    val a = Matrix {
      row(1, 2, 3, 4)
      row(5, 6, 7, 8)
      row(9, 8, 7, 6)
      row(5, 4, 3, 2)
    }

    val b = Matrix {
      row(-2, 1, 2, 3)
      row(3, 2, 1, -1)
      row(4, 3, 6, 5)
      row(1, 2, 7, 8)
    }

    val r = a * b

    assertEquals(actual = r, expected = Matrix {
      row(20, 22, 50, 48)
      row(44, 54, 114, 108)
      row(40, 58, 110, 102)
      row(16, 26, 46, 42)
    })
  }

  @JsName("A_matrix_multiplied_by_a_tuple")
  @Test
  fun `A matrix multiplied by a tuple`() {
    val A = Matrix {
      row(1, 2, 3, 4)
      row(2, 4, 4, 2)
      row(8, 6, 4, 1)
      row(0, 0, 0, 1)
    }

    val b = Tuple(1, 2, 3, 1)

    val result = A * b
    assertEquals(actual = result, expected = Tuple(18, 24, 33, 1))
  }

  @JsName("Multiplying_a_matrix_by_the_identity_matrix")
  @Test
  fun `Multiplying a matrix by the identity matrix`() {
    val A = Matrix {
      row(0, 1, 2, 4)
      row(1, 2, 4, 8)
      row(2, 4, 8, 16)
      row(4, 8, 16, 32)
    }

    assertEquals(actual = A * IDENTITY, expected = A)
  }

  @JsName("Transposing_a_matrix")
  @Test
  fun `Transposing a matrix`() {
    val A = Matrix {
      row(0, 9, 3, 0)
      row(9, 8, 0, 8)
      row(1, 8, 5, 3)
      row(0, 0, 5, 8)
    }

    val expected = Matrix {
      row(0, 9, 1, 0)
      row(9, 8, 8, 0)
      row(3, 0, 5, 5)
      row(0, 8, 3, 8)
    }

    assertEquals(actual = A.transpose(), expected = expected)
  }

  @JsName("Transposing_the_identity_matrix")
  @Test
  fun `Transposing the identity matrix`() {
    assertEquals(actual = IDENTITY.transpose(), expected = IDENTITY)
  }

  @JsName("Calculating_the_determinant_of_a_2x2_matrix")
  @Test
  fun `Calculating the determinant of a 2x2 matrix`() {
    val A = Matrix {
      row(1, 5)
      row(-3, 2)
    }

    assertEquals(actual = A.determinant, expected = 17f)
  }

  @JsName("A_submatrix_of_a_3x3_matrix_is_a_2x2_matrix")
  @Test
  fun `A submatrix of a 3x3 matrix is a 2x2 matrix`() {
    val A = Matrix {
      row(1, 5, 0)
      row(-3, 2, 7)
      row(0, 6, -3)
    }

    assertEquals(actual = A.subMatrixOf(0, 2), expected = Matrix {
      row(-3, 2)
      row(0, 6)
    })
  }

  @JsName("A_submatrix_of_a_4x4_matrix_is_a_3x3_matrix")
  @Test
  fun `A submatrix of a 4x4 matrix is a 3x3 matrix`() {
    val A = Matrix {
      row(-6, 1, 1, 6)
      row(-8, 5, 8, 6)
      row(-1, 0, 8, 2)
      row(-7, 1, -1, 1)
    }

    assertEquals(actual = A.subMatrixOf(2, 1), expected = Matrix {
      row(-6, 1, 6)
      row(-8, 8, 6)
      row(-7, -1, 1)
    })
  }

  @JsName("Calculating_a_minor_of_a_3x3_matrix")
  @Test
  fun `Calculating a minor of a 3x3 matrix`() {
    val A = Matrix {
      row(3, 5, 0)
      row(2, -1, -7)
      row(6, -1, 5)
    }

    val B = A.subMatrixOf(1, 0)
    val detB = B.determinant

    assertEquals(actual = A.minor(1, 0), expected = detB)
  }

  @JsName("Calculating_a_cofactor_of_a_3x3_matrix")
  @Test
  fun `Calculating a cofactor of a 3x3 matrix`() {
    val A = Matrix {
      row(3, 5, 0)
      row(2, -1, -7)
      row(6, -1, 5)
    }

    assertEquals(actual = A.minor(0, 0), expected = -12f)
    assertEquals(actual = A.cofactor(0, 0), expected = -12f)
    assertEquals(actual = A.minor(1, 0), expected = 25f)
    assertEquals(actual = A.cofactor(1, 0), expected = -25f)
  }

  @JsName("Calculating_the_determinant_of_a_3x3_matrix")
  @Test
  fun `Calculating the determinant of a 3x3 matrix`() {
    val A = Matrix {
      row(1, 2, 6)
      row(-5, 8, -4)
      row(2, 6, 4)
    }

    assertEquals(actual = A.cofactor(0, 0), expected = 56f)
    assertEquals(actual = A.cofactor(0, 1), expected = 12f)
    assertEquals(actual = A.cofactor(0, 2), expected = -46f)
    assertEquals(actual = A.determinant, expected = -196f)
  }

  @JsName("Calculating_the_determinant_of_a_4x4_matrix")
  @Test
  fun `Calculating the determinant of a 4x4 matrix`() {
    val A = Matrix {
      row(-2, -8, 3, 5)
      row(-3, 1, 7, 3)
      row(1, 2, -9, 6)
      row(-6, 7, 7, -9)
    }

    assertEquals(actual = A.cofactor(0, 0), expected = 690f)
    assertEquals(actual = A.cofactor(0, 1), expected = 447f)
    assertEquals(actual = A.cofactor(0, 2), expected = 210f)
    assertEquals(actual = A.cofactor(0, 3), expected = 51f)
    assertEquals(actual = A.determinant, expected = -4071f)
  }

  @JsName("Testing_an_invertible_matrix_for_invertibility")
  @Test
  fun `Testing an invertible matrix for invertibility`() {
    val A = Matrix {
      row(6, 4, 4, 4)
      row(5, 5, 7, 6)
      row(4, -9, 3, -7)
      row(9, 1, 7, -6)
    }

    assertEquals(actual = A.determinant, expected = -2120f)
    assertEquals(actual = A.isInvertible, expected = true)
  }

  @JsName("Testing_a_non_invertible_matrix_for_invertibility")
  @Test
  fun `Testing a non-invertible matrix for invertibility`() {
    val A = Matrix {
      row(-4, 2, -2, -3)
      row(9, 6, 2, 6)
      row(0, -5, 1, -5)
      row(0, 0, 0, 0)
    }

    assertEquals(actual = A.determinant, expected = 0f)
    assertEquals(A.isInvertible, false)
  }

  @JsName("Calculating_the_inverse_of_a_matrix")
  @Test
  fun `Calculating the inverse of a matrix`() {
    val A = Matrix {
      row(-5, 2, 6, -8)
      row(1, -5, 1, 8)
      row(7, 7, -6, -7)
      row(1, -3, 7, 4)
    }

    assertEquals(actual = A.cofactor(0, 0), expected = 116f)
    assertEquals(actual = A.cofactor(0, 1), expected = -430f)
    assertEquals(actual = A.cofactor(0, 2), expected = -42f)
    assertEquals(actual = A.cofactor(0, 3), expected = -278f)

    assertEquals(actual = A.cofactor(1, 0), expected = 240f)
    assertEquals(actual = A.cofactor(1, 1), expected = -775f)
    assertEquals(actual = A.cofactor(1, 2), expected = -119f)
    assertEquals(actual = A.cofactor(1, 3), expected = -433f)

    assertEquals(actual = A.cofactor(2, 0), expected = 128f)
    assertEquals(actual = A.cofactor(2, 1), expected = -236f)
    assertEquals(actual = A.cofactor(2, 2), expected = -28f)
    assertEquals(actual = A.cofactor(2, 3), expected = -160f)

    assertEquals(actual = A.cofactor(3, 0), expected = -24f)
    assertEquals(actual = A.cofactor(3, 1), expected = 277f)
    assertEquals(actual = A.cofactor(3, 2), expected = 105f)
    assertEquals(actual = A.cofactor(3, 3), expected = 163f)

    val B = A.inverse()

    assertEquals(actual = A.determinant, expected = 532f)
    assertEquals(actual = B[3, 2], expected = -160f / 532f)
    assertEquals(actual = B[2, 3], expected = 105f / 532f)

    val expected = Matrix {
      row(0.21805, 0.45113, 0.24060, -0.04511)
      row(-0.80827, -1.45677, -0.44361, 0.52068)
      row(-0.07895, -0.22368, -0.05263, 0.19737)
      row(-0.52256, -0.81391, -0.30075, 0.30639)
    }

    assertMatrixEquals(actual = B, expected = expected)
  }

  @JsName("Calculating_the_inverse_of_another_matrix")
  @Test
  fun `Calculating the inverse of another matrix`() {
    val A = Matrix {
      row(8, -5, 9, 2)
      row(7, 5, 6, 1)
      row(-6, 0, 9, 6)
      row(-3, 0, -9, -4)
    }

    val B = A.inverse()

    val expected = Matrix {
      row(-0.15385, -0.15385, -0.28205, -0.53846)
      row(-0.07692, 0.12308, 0.02564, 0.03077)
      row(0.35897, 0.35897, 0.43590, 0.92308)
      row(-0.69231, -0.69231, -0.76923, -1.92308)
    }

    assertMatrixEquals(actual = B, expected = expected)
  }

  @JsName("Calculating_the_inverse_of_a_third_matrix")
  @Test
  fun `Calculating the inverse of a third matrix`() {
    val A = Matrix {
      row(9, 3, 0, 9)
      row(-5, -2, -6, -3)
      row(-4, 9, 6, 4)
      row(-7, 6, 6, 2)
    }

    val B = A.inverse()

    val expected = Matrix {
      row(-0.04074, -0.07778, 0.14444, -0.22222)
      row(-0.07778, 0.03333, 0.36667, -0.33333)
      row(-0.02901, -0.14630, -0.10926, 0.12963)
      row(0.17778, 0.06667, -0.26667, 0.33333)
    }

    assertMatrixEquals(actual = B, expected = expected)
  }

  @JsName("Multiplying_a_product_by_its_inverse")
  @Test
  fun `Multiplying a product by its inverse`() {
    val A = Matrix {
      row(3, -9, 7, 3)
      row(3, -8, 2, -9)
      row(-4, 4, 4, 1)
      row(-6, 5, -1, 1)
    }

    val B = Matrix {
      row(8, 2, 2, 2)
      row(3, -1, 7, 0)
      row(7, 0, 5, 4)
      row(6, -2, 0, 5)
    }

    val C = A * B
    assertMatrixEquals(actual = C * B.inverse(), expected = A)
  }

  @JsName("Multiplying_by_a_translation_matrix")
  @Test
  fun `Multiplying by a translation matrix`() {
    val transform = Matrix.translation(5, -3, 2)
    val p = Point(-3, 4, 5)
    assertEquals(actual = transform * p, expected = Point(2, 1, 7))
  }

  @JsName("Multiplying_by_the_inverse_of_a_translation_matrix")
  @Test
  fun `Multiplying by the inverse of a translation matrix`() {
    val transform = Matrix.translation(5, -3, 2)
    val inv = transform.inverse()
    val p = Point(-3, 4, 5)
    assertEquals(actual = inv * p, expected = Point(-8, 7, 3))
  }

  @JsName("Translation_does_not_affect_vectors")
  @Test
  fun `Translation does not affect vectors`() {
    val transform = Matrix.translation(5, -3, 2)
    val v = Vector(-3, 4, 5)
    assertEquals(actual = transform * v, expected = v)
  }

  @JsName("A_scaling_matrix_applied_to_a_point")
  @Test
  fun `A scaling matrix applied to a point`() {
    val transform = Matrix.scaling(2, 3, 4)
    val p = Point(-4, 6, 8)
    assertEquals(actual = transform * p, expected = Point(-8, 18, 32))
  }

  @JsName("A_scaling_matrix_applied_to_a_vector")
  @Test
  fun `A scaling matrix applied to a vector`() {
    val transform = Matrix.scaling(2, 3, 4)
    val v = Vector(-4, 6, 8)
    assertEquals(actual = transform * v, expected = Vector(-8, 18, 32))
  }

  @JsName("Multiplying_by_the_inverse_of_a_scaling_matrix")
  @Test
  fun `Multiplying by the inverse of a scaling matrix`() {
    val transform = Matrix.scaling(2, 3, 4)
    val inv = transform.inverse()
    val v = Vector(-4, 6, 8)
    assertEquals(actual = inv * v, expected = Vector(-2, 2, 2))
  }

  @JsName("Reflection_is_scaling_by_a_negative_value")
  @Test
  fun `Reflection is scaling by a negative value`() {
    val transform = Matrix.scaling(-1, 1, 1)
    val p = Point(2, 3, 4)
    assertEquals(actual = transform * p, expected = Point(-2, 3, 4))
  }

  @JsName("Rotating_a_point_around_the_x_axis")
  @Test
  fun `Rotating a point around the x axis`() {
    val p = Point(0, 1, 0)
    val halfQuarter = Matrix.rotationX(PI / 4.0)
    val fullQuarter = Matrix.rotationX(PI / 2.0)
    assertTupleEquals(actual = halfQuarter * p, expected = Point(0, sqrt(2f) / 2f, sqrt(2f) / 2))
    assertTupleEquals(actual = fullQuarter * p, expected = Point(0, 0, 1))
  }

  @JsName("The_inverse_of_an_x_rotation_rotates_in_the_opposite_direction")
  @Test
  fun `The inverse of an x-rotation rotates in the opposite direction`() {
    val p = Point(0, 1, 0)
    val halfQuarter = Matrix.rotationX(PI / 4.0)
    val inv = halfQuarter.inverse()
    assertTupleEquals(actual = inv * p, expected = Point(0, sqrt(2f) / 2f, -sqrt(2f) / 2f))
  }

  @JsName("Rotating_a_point_around_the_y_axis")
  @Test
  fun `Rotating a point around the y axis`() {
    val p = Point(0, 0, 1)
    val halfQuarter = Matrix.rotationY(PI / 4.0)
    val fullQuarter = Matrix.rotationY(PI / 2.0)
    assertTupleEquals(actual = halfQuarter * p, expected = Point(sqrt(2f) / 2f, 0, sqrt(2f) / 2f))
    assertTupleEquals(actual = fullQuarter * p, expected = Point(1, 0, 0))
  }

  @JsName("Rotating_a_point_around_the_z_axis")
  @Test
  fun `Rotating a point around the z axis`() {
    val p = Point(0, 1, 0)
    val halfQuarter = Matrix.rotationZ(PI / 4.0)
    val fullQuarter = Matrix.rotationZ(PI / 2.0)
    assertTupleEquals(actual = halfQuarter * p, expected = Point(-sqrt(2f) / 2, sqrt(2f) / 2, 0))
    assertTupleEquals(actual = fullQuarter * p, expected = Point(-1, 0, 0))
  }

  @JsName("A_shearing_transformation_moves_x_in_proportion_to_y")
  @Test
  fun `A shearing transformation moves x in proportion to y`() {
    val transform = Matrix.shearing(1, 0, 0, 0, 0, 0)
    val p = Point(2, 3, 4)
    assertTupleEquals(actual = transform * p, expected = Point(5, 3, 4))
  }

  @JsName("A_shearing_transformation_moves_x_in_proportion_to_z")
  @Test
  fun `A shearing transformation moves x in proportion to z`() {
    val transform = Matrix.shearing(0, 1, 0, 0, 0, 0)
    val p = Point(2, 3, 4)
    assertTupleEquals(actual = transform * p, expected = Point(6, 3, 4))
  }

  @JsName("A_shearing_transformation_moves_y_in_proportion_to_x")
  @Test
  fun `A shearing transformation moves y in proportion to x`() {
    val transform = Matrix.shearing(0, 0, 1, 0, 0, 0)
    val p = Point(2, 3, 4)
    assertTupleEquals(actual = transform * p, expected = Point(2, 5, 4))
  }

  @JsName("A_shearing_transformation_moves_y_in_proportion_to_z")
  @Test
  fun `A shearing transformation moves y in proportion to z`() {
    val transform = Matrix.shearing(0, 0, 0, 1, 0, 0)
    val p = Point(2, 3, 4)
    assertTupleEquals(actual = transform * p, expected = Point(2, 7, 4))
  }

  @JsName("A_shearing_transformation_moves_z_in_proportion_to_x")
  @Test
  fun `A shearing transformation moves z in proportion to x`() {
    val transform = Matrix.shearing(0, 0, 0, 0, 1, 0)
    val p = Point(2, 3, 4)
    assertTupleEquals(actual = transform * p, expected = Point(2, 3, 6))
  }

  @JsName("A_shearing_transformation_moves_z_in_proportion_to_y")
  @Test
  fun `A shearing transformation moves z in proportion to y`() {
    val transform = Matrix.shearing(0, 0, 0, 0, 0, 1)
    val p = Point(2, 3, 4)
    assertTupleEquals(actual = transform * p, expected = Point(2, 3, 7))
  }

  @JsName("Individual_transformations_are_applied_in_sequence")
  @Test
  fun `Individual transformations are applied in sequence`() {
    val p = Point(1, 0, 1)
    val A = Matrix.rotationX(PI / 2)
    val B = Matrix.scaling(5, 5, 5)
    val C = Matrix.translation(10, 5, 7)

    // apply rotation first​
    val p2 = A * p
    assertTupleEquals(actual = p2, expected = Point(1, -1, 0))

    // then apply scaling​
    val p3 = B * p2
    assertTupleEquals(actual = p3, expected = Point(5, -5, 0))

    // then apply translaction
    val p4 = C * p3
    assertTupleEquals(actual = p4, expected = Point(15, 0, 7))
  }

  @JsName("Chained_transformations_must_be_applied_in_reverse_order")
  @Test
  fun `Chained transformations must be applied in reverse order`() {
    val p = Point(1, 0, 1)
    val A = Matrix.rotationX(PI / 2)
    val B = Matrix.scaling(5, 5, 5)
    val C = Matrix.translation(10, 5, 7)
    val T = C * B * A
    assertTupleEquals(actual = T * p, expected = Point(15, 0, 7))
  }
}
