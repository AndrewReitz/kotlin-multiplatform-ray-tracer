package raytracer.core

import raytracer.math.*
import raytracer.test.assertFloat3Equals
import raytracer.test.assertMatrixEquals
import kotlin.js.JsName
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class ShapeTest {

    class TestShape(
        override val material: Material = Material(),
        override val transform: Matrix = Matrix.IDENTITY
    ): Shape {

        private var _savedRay: Ray? = null

        val savedRay: Ray get() = requireNotNull(_savedRay)

        override fun localIntersect(localRay: Ray): Intersections {
            _savedRay = localRay
            return Intersections.EMPTY
        }

        override fun localNormalAt(localPoint: Point): Vector3 {
            return localPoint.toVector()
        }
    }

    @JsName("The_default_transformation")
    @Test
    fun `The default transformation`() {
        val s = TestShape()
        assertMatrixEquals(actual = s.transform, expected = Matrix.IDENTITY)
    }

    @JsName("Assigning_a_transformation")
    @Test
    fun `Assigning a transformation`() {
        val s = TestShape(transform = Matrix.translation(2, 3, 4))
        assertMatrixEquals(actual = s.transform, expected = Matrix.translation(2, 3, 4))
    }

    @JsName("The_default_material")
    @Test
    fun `The default material`() {
        val s = TestShape()
        assertEquals(actual = s.material, expected = Material())
    }

    @JsName("Assigning_a_material")
    @Test
    fun `Assigning a material`() {
        val m = Material(ambient = 1)
        val s = TestShape(material = m)
        assertEquals(actual = s.material, expected = m)
    }

    @JsName("Intersecting_a_scaled_shape_with_a_ray")
    @Test
    fun `Intersecting a scaled shape with a ray`() {
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = TestShape(transform = Matrix.scaling(2, 2, 2))
        s.intersect(r)
        assertFloat3Equals(actual = s.savedRay.origin, expected = Point(0, 0, -2.5))
    }

    @JsName("Intersecting_a_translated_shape_with_a_ray")
    @Test
    fun `Intersecting a translated shape with a ray`() {
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = TestShape(transform = Matrix.translation(5, 0, 0))
        s.intersect(r)
        assertFloat3Equals(actual = s.savedRay.origin, expected = Point(-5, 0, -5))
        assertFloat3Equals(actual = s.savedRay.direction, expected = Vector(0, 0, 1))
    }

    @JsName("Computing_the_normal_on_a_translated_shape")
    @Test
    fun `Computing the normal on a translated shape`() {
        val s = TestShape(transform = Matrix.translation(0, 1, 0))
        val n = s.normalAt(Point(0, 1.70711, -0.70711))
        assertFloat3Equals(actual = n, expected = Vector(0, 0.70711, -0.70711))
    }

    @JsName("Computing_the_normal_on_a_transformed_shape")
    @Test
    fun `Computing the normal on a transformed shape`() {
        val s = TestShape(
            transform = Matrix.scaling(1, 0.5, 1) * Matrix.rotationZ(PI / 5)
        )
        val n = s.normalAt(Point(0, sqrt(2f) / 2, -sqrt(2f) / 2))
        assertFloat3Equals(actual = n , expected = Vector(0, 0.97014, -0.24254))
    }
}
