package raytracer.math

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class MatrixTest {
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

  @Test
  fun `Multiplying a matrix by the identity matrix`() {
    val A = Matrix {
      row(0, 1, 2, 4)
      row(1, 2, 4, 8)
      row(2, 4, 8, 16)
      row(4, 8, 16, 32)
    }

    assertEquals(actual = A * IDENTITY_MATRIX, expected = A)
  }

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

  @Test
  fun `Transposing the identity matrix`() {
    assertEquals(actual = IDENTITY_MATRIX.transpose(), expected = IDENTITY_MATRIX)
  }

  @Test
  fun `Calculating the determinant of a 2x2 matrix`() {
    val A = Matrix {
      row(1, 5)
      row(-3, 2)
    }

    assertEquals(actual = A.determinant, expected = 17f)
  }

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

    assertTrue("Expected <$expected>, actual <$B>.") { B.fuzzyEquals(expected) }
  }

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

  /** For fuzzy assertions around floating points */
  private fun assertMatrixEquals(actual: Matrix, expected: Matrix) {
    assertTrue("Expected <$expected>, actual <$actual>.") { actual.fuzzyEquals(expected) }
  }
}
