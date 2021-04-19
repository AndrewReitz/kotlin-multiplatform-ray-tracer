package raytracer.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import raytracer.core.Camera
import raytracer.core.Color
import raytracer.core.Material
import raytracer.core.PointLight
import raytracer.core.Scene
import raytracer.core.World
import raytracer.core.shapes.Sphere
import raytracer.core.viewTransform
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertMatrixEquals
import kotlin.js.JsName
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonSerializationTest {
    @Test
    @JsName("should_be_bi_directional")
    fun `should be bi-directional`() {
        val sphere = Sphere(
            material = Material(color = Color.Blue, shininess = 30, specular = 0.5, ambient = 0),
            transform = Matrix.translation(0, 1.5, 1) * Matrix.scaling(x = 1, y = 3, z = 1)
        )

        val world = World(
            lights = listOf(
                PointLight(
                    Point(-1, 20, -10),
                    Color.White
                )
            ),
            objects = listOf(sphere)
        )

        val camera = Camera(
            hsize = 500,
            vsize = 500,
            fieldOfView = PI / 3,
            transform = viewTransform(
                from = Point(-1, 1, -10),
                to = Point(0, 0.5, 4),
                up = Vector(0, 1, 0)
            )
        )

        val scene = Scene(camera = camera, world = world)
        val json = JSON.encodeToString(scene)
        assertTrue { json.isNotEmpty() }

        val result: Scene = JSON.decodeFromString(json)

        assertEquals(expected = scene.camera.hsize, actual = result.camera.hsize)
        assertEquals(expected = scene.camera.vsize, actual = result.camera.vsize)
        assertEquals(expected = scene.camera.fieldOfView, actual = result.camera.fieldOfView)
        assertMatrixEquals(expected = scene.camera.transform, actual = result.camera.transform)
        assertEquals(expected = scene.world, actual = result.world)
    }
}
