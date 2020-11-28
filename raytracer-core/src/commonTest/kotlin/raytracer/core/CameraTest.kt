package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import raytracer.test.assertFloatsEquals
import kotlin.js.JsName
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class CameraTest {
    @JsName("Constructing_a_camera")
    @Test
    fun `Constructing a camera`() {
        val hsize = 160
        val vsize = 210
        val fieldOfView = PI / 2.0

        val c = Camera(hsize, vsize, fieldOfView)
        assertEquals(actual = c.hsize, expected = hsize)
        assertEquals(actual = c.vsize, expected = vsize)
        assertFloatsEquals(actual = c.fieldOfView, expected = fieldOfView)
        assertEquals(actual = c.transform, expected = Matrix.IDENTITY)
    }

    @JsName("The_pixel_size_for_a_horizontal_canvas")
    @Test
    fun `The pixel size for a horizontal canvas`() {
        val c = Camera(200, 125, PI / 2)
        assertFloatsEquals(actual = c.pixelSize, expected = 0.01)
    }

    @JsName("The_pixel_size_for_a_vertical_canvas")
    @Test
    fun `The pixel size for a vertical canvas`() {
        val c = Camera(125, 200, PI / 2)
        assertFloatsEquals(actual = c.pixelSize, expected = 0.01)
    }

    @JsName("Constructing_a_ray_through_the_center_of_the_canvas")
    @Test
    fun `Constructing a ray through the center of the canvas`() {
        val c = Camera(201, 101, PI / 2)
        val r = c.rayForPixel(100, 50)
        assertFloat3Equals(actual = r.origin, expected = Point(0, 0, 0))
        assertFloat3Equals(actual = r.direction, expected = Point(0, 0, -1))
    }

    @JsName("Constructing_a_ray_through_a_corner_of_the_canvas")
    @Test
    fun `Constructing a ray through a corner of the canvas`() {
        val c = Camera(201, 101, PI / 2)
        val r = c.rayForPixel(0, 0)
        assertFloat3Equals(actual = r.origin, expected = Point(0, 0, 0))
        assertFloat3Equals(actual = r.direction, expected = Vector(0.66519, 0.33259, -0.66851))
    }

    @JsName("Constructing_a_ray_when_the_camera_is_transformed")
    @Test
    fun `Constructing a ray when the camera is transformed`() {
        val c = Camera(
            hsize = 201,
            vsize = 101,
            fieldOfView = PI / 2,
            transform = Matrix.rotationY(PI / 4) * Matrix.translation(0, -2, 5)
        )
        val r = c.rayForPixel(100, 50)
        assertFloat3Equals(actual = r.origin, expected = Point(0, 2, -5))
        assertFloat3Equals(actual = r.direction, expected = Vector(sqrt(2.0) / 2, 0, -sqrt(2.0) / 2))
    }

    @JsName("Rendering_a_world_with_a_camera")
    @Test
    fun `Rendering a world with a camera`() = raytracer.core.runBlocking {
        val w = World.default
        val from = Point(0, 0, -5)
        val to = Point(0, 0, 0)
        val up = Vector(0, 1, 0)
        val c = Camera(11, 11, PI / 2, viewTransform(from, to, up))
        val image = c.render(w)
        assertFloat3Equals(actual = image[5, 5], expected = Color(0.38066, 0.47583, 0.2855))
    }
}
