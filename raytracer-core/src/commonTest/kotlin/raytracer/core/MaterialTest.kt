package raytracer.core

import raytracer.core.patterns.StripePattern
import raytracer.core.shapes.GlassSphere
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import raytracer.test.assertFloatsEquals
import raytracer.test.assertMatrixEquals
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

    @JsName("Lighting_with_a_pattern_applied")
    @Test
    fun `Lighting with a pattern applied`() {
        val m = Material(
            ambient = 1,
            diffuse = 0,
            specular = 0,
            pattern = StripePattern(Color.White, Color.Black)
        )

        val eyev = Vector(0, 0, -1)
        val normalv = Vector(0, 0, -1)
        val light = PointLight(Point(0, 0, -10), Color.White)
        val c1 = m.lighting(light, Point(0.9, 0, 0), eyev, normalv, false)
        val c2 = m.lighting(light, Point(1.1, 0, 0), eyev, normalv, false)
        assertEquals(actual = c1, expected = Color.White)
        assertEquals(actual = c2, expected = Color.Black)
    }

    @JsName("Reflectivity_for_the_default_material")
    @Test
    fun `Reflectivity for the default material`() {
        assertFloatsEquals(actual = Material().reflective, expected = 0)
    }

    @JsName("Transparency_and_Refractive_Index_for_the_default_material")
    @Test
    fun `Transparency and Refractive Index for the default material`() {
        val m = Material()
        assertFloatsEquals(actual = m.transparency, expected = 0)
        assertFloatsEquals(actual = m.refractiveIndex, expected = 1)
    }

    @JsName("A_helper_for_producing_a_sphere_with_a_glassy_material")
    @Test
    fun `A helper for producing a sphere with a glassy material`() {
        val s = GlassSphere()
        assertMatrixEquals(actual = s.transform, expected = Matrix.IDENTITY)
        assertFloatsEquals(actual = s.material.transparency, expected = 1)
        assertFloatsEquals(actual = s.material.refractiveIndex, expected = 1.5)
    }

    @JsName("Finding_n1_and_n2_at_various_intersections")
    @Test
    fun `Finding n1 and n2 at various intersections`() {
        fun check(index: Int, n1: Number, n2: Number) {
            val A = GlassSphere(transform = Matrix.scaling(2, 2, 2))
            val B = GlassSphere(
                transform = Matrix.translation(0, 0, 0.25),
                material = Material(refractiveIndex = 2.0, transparency = 1.0)
            )
            val C = GlassSphere(
                transform = Matrix.translation(0, 0, 0.25),
                material = Material(refractiveIndex = 2.5, transparency = 1.0)
            )
            val r = Ray(Point(0, 0, -4), Vector(0, 0, 0.25))
            val xs = Intersections(
                Intersection(2, A),
                Intersection(2.75, B),
                Intersection(3.25, C),
                Intersection(4.75, B),
                Intersection(5.25, C),
                Intersection(6, A)
            )
            val comps = xs[index].prepareComputations(r, xs)
            assertFloatsEquals(actual = comps.n1, expected = n1, message = "Expected n1:<$n1>, actual <${comps.n1}> at index <$index>.")
            assertFloatsEquals(actual = comps.n2, expected = n2, message = "Expected n2<$n2>, actual <${comps.n1}> at index <$index>.")
        }

        check(0, 1.0, 1.5)
        check(1, 1.5, 2.0)
        check(2, 2.0, 2.5)
        check(3, 2.5, 2.5)
        check(4, 2.5, 1.5)
        check(5, 1.5, 1.0)
    }
}
