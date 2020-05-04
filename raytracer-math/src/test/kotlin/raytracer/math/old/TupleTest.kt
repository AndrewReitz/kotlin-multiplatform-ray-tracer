package raytracer.math.old

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import raytracer.math.Point
import raytracer.math.Vector
import kotlin.test.assertEquals

class TupleTest {

  @Test fun `Adding a vector to a point`() {
    val a1 = Point(3, -2, 5)
    val a2 = Vector(-2, 3, 1)

    val result = a1 + a2

    assertEquals(expected = Point(1, 1, 6), actual = result)
  }

  @Test fun `Adding a point to a vector`() {
    val a1 = Vector(-2, 3, 1)
    val a2 = Point(3, -2, 5)

    val result = a1 + a2

    assertEquals(expected = Point(1, 1, 6), actual = result)
  }

  @Test fun `Adding a vector to a vector`() {
    val a1 = Vector(-2, 3, 1)
    val a2 = Vector(3, -2, 5)

    val result = a1 + a2

    assertEquals(expected = Vector(1, 1, 6), actual = result)
  }

  @Test fun `Subtracting two points`() {
    val p1 = Point(3, 2, 1)
    val p2 = Point(5, 6, 7)

    val result = p1 - p2

    assertEquals(expected = Vector(-2, -4, -6), actual = result)
  }

  @Test fun `Subtracting a vector from a point`() {
    val p = Point(3, 2, 1)
    val v = Vector(5, 6, 7)

    val result = p - v

    assertEquals(expected = Point(-2, -4, -6), actual = result)
  }

  @Test fun `Subtracting two vectors`() {
    val v1 = Vector(3, 2, 1)
    val v2 = Vector(5, 6, 7)

    val result = v1 - v2

    assertEquals(expected = Vector(-2, -4, -6), actual = result)
  }

  @Test fun `Subtracting a vector from the zero vector`() {
    val zero = Vector(0, 0, 0)
    val v = Vector(1, -2, 3)

    val result = zero - v

    assertEquals(expected = Vector(-1, 2, -3), actual = result)
  }

  @Disabled
  @Test fun `Negating a point`() {
    assertEquals(expected = Point(
      -1,
      2,
      3
    ), actual = !Point(1, -2, 3)
    )
  }

  @Disabled
  @Test fun `Negating a vector`() {
    assertEquals(expected = Vector(
      -1,
      2,
      3
    ), actual = !Vector(1, -2, 3)
    )
  }
}