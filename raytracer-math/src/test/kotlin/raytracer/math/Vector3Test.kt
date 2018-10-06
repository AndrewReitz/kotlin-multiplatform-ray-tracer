package raytracer.math

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class Vector3Test {

    @Test fun `should add two vectors together`() {
        val expected = Vector3(22, 6, -12)
        val a = Vector3(5, 9, -10)
        val b = Vector3(17, -3, -2)

        val result = a + b

        assertEquals(expected = expected, actual = result)
    }

    @Test fun `should subtract a vector from another`() {
        val expected = Vector3(28, -4, 13)
        val a = Vector3(18, 5, 3)
        val b = Vector3(-10, 9, -10)

        val result = a - b

        assertEquals(actual = result, expected = expected)
    }

    @Test fun `should multiply a vector by an amount`() {
        val expected = Vector3(30, 20, 10)
        val a = Vector3(3, 2, 1)

        val result = a * 10.0

        assertEquals(expected = expected, actual = result)
    }

    @Test fun `should divide a vector by an amount`() {
        val expected = Vector3(3, 2, 1)
        val a = Vector3(30, 20, 10)

        val result = a / 10.0

        assertEquals(actual = result, expected = expected)
    }

    @Test fun `should throw IllegalStateException when trying to divide by 0`() {
        assertThrows<IllegalStateException>("Cannot divide by 0") {
            val vector = Vector3()
            vector / 0.0
        }
    }

    @Test fun `should negate a vector`() {
        val expected = Vector3(-1, -2, 3)
        val vector = Vector3(1, 2, -3)

        val actual = !vector

        assertEquals(actual = actual, expected = expected)
    }

    @Test fun `should calculate the dot product`() {
        val a = Vector3(3, 4, 5)
        val b = Vector3(4, 3, 5)

        val result = a dot b

        assertEquals(actual = result, expected = 49.0)
    }

    @Test fun `should calculate the cross product`() {
        val expected = Vector3(5, 5, -7)
        val a = Vector3(3, 4, 5)
        val b = Vector3(4, 3, 5)

        val result = a cross b

        assertEquals(actual = result, expected = expected)
    }

    @Test fun `should get the length`() {
        val a = Vector3(3, 4, 5)

        val result = a.length

        assertEquals(actual = result, expected = 7.0710678118654755)
    }

    @Test fun `should normalize the vector`() {
        val expected = Vector3(
                x = 0.577350269189625709141587549845781707447655068136,
                y = 0.577350269189625709141587549845781707447655068136,
                z = 0.577350269189625709141587549845781707447655068136
        )
        val a = Vector3(10, 10, 10)

        val result = a.normalize

        assertEquals(actual = result, expected = expected)
    }
}