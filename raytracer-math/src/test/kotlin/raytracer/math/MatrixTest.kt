package raytracer.math

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

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
  fun `Transposing the identiy matrix`() {
    assertEquals(actual = IDENTITY_MATRIX.transpose(), expected = IDENTITY_MATRIX)
  }
}
