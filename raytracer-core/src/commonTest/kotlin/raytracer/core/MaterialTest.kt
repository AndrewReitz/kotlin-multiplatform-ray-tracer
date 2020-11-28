package raytracer.core

import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class MaterialTest {

    private val m = Material()
    private val position = Point(0, 0, 0)

    @JsName("The_default_material")
    @Test
    fun `The default material`() {
        assertEquals(actual = m.color, expected = Color(1, 1, 1))
        assertEquals(actual = m.ambient, expected = 0.1f)
        assertEquals(actual = m.diffuse, expected = 0.9f)
        assertEquals(actual = m.specular, expected = 0.9f)
        assertEquals(actual = m.shininess, expected = 200f)
    }

    @JsName("Lighting_with_the_eye_between_the_light_and_the_surface")
    @Test
    fun `Lighting with the eye between the light and the surface`() {
        val eyev = Vector(0, 0, -1)
        val normalv = Vector(0, 0, -1)
        val light = PointLight(
            position = Point(0, 0, -10),
            intensity = Color(1, 1, 1)
        )
        val result = m.lighting(light, position, eyev, normalv)
        assertFloat3Equals(actual = result, expected = Color(1.9, 1.9, 1.9))
    }

    @JsName("Lighting_with_the_eye_between_light_and_surface_eye_offset_45")
    @Test
    fun `Lighting with the eye between light and surface, eye offset 45 degrees`() {
        val eyev = Vector(0, sqrt(2f) / 2, -sqrt(2f) / 2)
        val normalv = Vector(0, 0, -1)
        val light = PointLight(Point(0, 0, -10), Color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        assertFloat3Equals(actual = result, expected = Color(1, 1, 1))
    }

    @JsName("Lighting_with_eye_opposite_surface_light_offset_45_degrees")
    @Test
    fun `Lighting with eye opposite surface, light offset 45 degrees`() {
        val eyev = Vector(0, 0, -1)
        val normalv = Vector(0, 0, -1)
        val light = PointLight(Point(0, 10, -10), Color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        assertFloat3Equals(actual = result, expected = Color(0.7364, 0.7364, 0.7364))
    }

    @JsName("Lighting_with_eye_in_the_path_of_the_reflection_vector")
    @Test
    fun `Lighting with eye in the path of the reflection vector`() {
        val eyev = Vector(0, -sqrt(2f) / 2, -sqrt(2f) / 2)
        val normalv = Vector(0, 0, -1)
        val light = PointLight(Point(0, 10, -10), Color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        assertFloat3Equals(actual = result, expected = Color(1.63638, 1.63638, 1.63638))
    }

    @JsName("Lighting_with_the_light_behind_the_surface")
    @Test
    fun `Lighting with the light behind the surface`() {
        val eyev = Vector(0, 0, -1)
        val normalv = Vector(0, 0, -1)
        val light = PointLight(Point(0, 0, 10), Color(1, 1, 1))
        val result = m.lighting(light, position, eyev, normalv)
        assertFloat3Equals(actual = result, expected = Color(0.1, 0.1, 0.1))
    }

    @JsName("Lighting_with_the_surface_in_shadow")
    @Test
    fun `Lighting with the surface in shadow`() {
        val eyev = Vector(0, 0, -1)
        val normalv = Vector(0, 0, -1)
        val light = PointLight(
            position = Point(0, 0, -10),
            intensity = Color(1, 1, 1)
        )
        val inShadow = true
        val result = m.lighting(light, position, eyev, normalv, inShadow)
        assertFloat3Equals(actual = result, expected = Color(0.1, 0.1, 0.1))
    }
}
