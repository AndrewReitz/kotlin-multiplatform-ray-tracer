package raytracer.core

import raytracer.core.patterns.TestPattern
import raytracer.core.shapes.Plane
import raytracer.core.shapes.Sphere
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.SQUARE_ROOT_OF_2
import raytracer.math.SQUARE_ROOT_OF_2_OVER_2
import raytracer.math.Vector
import raytracer.test.assertFloat3Equals
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WorldTest {

    @JsName("Creating_a_world")
    @Test
    fun `Creating a world`() {
        val w = World()
        assertEquals(actual = w.lights, expected = emptyList())
        assertEquals(actual = w.objects, expected = emptyList())
    }

    @JsName("The_default_world")
    @Test
    fun `The default world`() {
        val light = PointLight(
            position = Point(-10, 10, -10),
            intensity = Color(1, 1, 1)
        )

        val s1 = Sphere(
            material = Material(
                color = Color(
                    red = 0.8,
                    green = 1.0,
                    blue = 0.6
                ),
                diffuse = 0.7f,
                specular = 0.2f
            )
        )

        val s2 = Sphere(
            transform = Matrix.scaling(0.5, 0.5, 0.5)
        )

        val w = World.default
        assertEquals(actual = w.lights, expected = listOf(light))
        assertEquals(actual = w.objects, expected = listOf(s1, s2))
    }

    @JsName("Intersect_a_world_with_a_ray")
    @Test
    fun `Intersect a world with a ray`() {
        val w = World.default
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val xs = w.intersect(r)
        assertEquals(actual = xs.size, expected = 4)
        assertEquals(actual = xs[0].time, expected = 4f)
        assertEquals(actual = xs[1].time, expected = 4.5f)
        assertEquals(actual = xs[2].time, expected = 5.5f)
        assertEquals(actual = xs[3].time, expected = 6f)
    }

    @JsName("Shading_an_intersection")
    @Test
    fun `Shading an intersection`() {
        val w = World.default
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val shape = w.objects.first()
        val i = Intersection(4, shape)
        val comps = i.prepareComputations(r)
        val c = w.shadeHit(comps, 5)
        assertFloat3Equals(actual = c, expected = Color(0.38066, 0.47583, 0.2855))
    }

    @JsName("Shading_an_intersection_from_the_inside")
    @Test
    fun `Shading an intersection from the inside`() {
        val w = World.default.copy(
            lights = listOf(
                PointLight(
                    position = Point(0, 0.25, 0),
                    intensity = Color.White
                )
            )
        )

        val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
        val shape = w.objects.take(2).last()
        val i = Intersection(0.5, shape)
        val comps = i.prepareComputations(r)
        val c = w.shadeHit(comps, 5)
        assertFloat3Equals(actual = c, expected = Color(0.90498, 0.90498, 0.90498))
    }

    @JsName("The_color_when_a_ray_misses")
    @Test
    fun `The color when a ray misses`() {
        val w = World.default
        val r = Ray(Point(0, 0, -5), Vector(0, 1, 0))
        val c = w.colorAt(r, 0)
        assertFloat3Equals(actual = c, expected = Color.Black)
    }

    @JsName("The_color_with_an_intersection_behind_the_ray")
    @Test
    fun `The color with an intersection behind the ray`() {
        val default = World.default
        val outer = default.objects.first() as Sphere
        val inner = default.objects.last() as Sphere

        val w = default.copy(
            objects = listOf(
                outer.copy(
                    material = outer.material.copy(ambient = 1f)
                ),
                inner.copy(
                    material = inner.material.copy(ambient = 1f)
                )
            )
        )

        val r = Ray(Point(0, 0, 0.75), Vector(0, 0, -1))
        val c = w.colorAt(r, 0)
        assertFloat3Equals(actual = c, expected = inner.material.color)
    }

    @JsName("There_is_no_shadow_when_nothing_is_collinear_with_point_and_light")
    @Test
    fun `There is no shadow when nothing is collinear with point and light`() {
        val w = World.default
        val p = Point(0, 10, 0)
        assertFalse(w.isShadowed(p, w.lights[0]))
    }

    @JsName("The_shadow_when_an_object_is_between_the_point_and_the_light")
    @Test
    fun `The shadow when an object is between the point and the light`() {
        val w = World.default
        val p = Point(10, -10, 10)
        assertTrue(w.isShadowed(p, w.lights[0]))
    }

    @JsName("There_is_no_shadow_when_an_object_is_behind_the_light")
    @Test
    fun `There is no shadow when an object is behind the light`() {
        val w = World.default
        val p = Point(-20, 20, -20)
        assertFalse(w.isShadowed(p, w.lights[0]))
    }

    @JsName("There_is_no_shadow_when_an_object_is_behind_the_point")
    @Test
    fun `There is no shadow when an object is behind the point`() {
        val w = World.default
        val p = Point(-2, 2, -2)
        assertFalse(w.isShadowed(p, w.lights[0]))
    }

    @JsName("shadeHit_is_given_an_intersection_in_shadow")
    @Test
    fun `shadeHit is given an intersection in shadow`() {
        val s1 = Sphere()
        val s2 = Sphere(
            transform = Matrix.translation(0, 0, 10)
        )

        val w = World.default
            .copy(
                objects = listOf(s1, s2),
                lights = listOf(
                    PointLight(
                        Point(0, 0, -10), Color(1, 1, 1)
                    )
                )
            )

        val r = Ray(Point(0, 0, 5), Vector(0, 0, 1))
        val i = Intersection(4, s2)

        val comps = i.prepareComputations(r)
        val c = w.shadeHit(comps, 5)
        assertFloat3Equals(actual = c, expected = Color(0.1, 0.1, 0.1))
    }

    @JsName("The_reflected_color_for_a_nonreflective_material")
    @Test
    fun `The reflected color for a nonreflective material`() {
        val shape = (World.default.objects.last() as Sphere).run {
            copy(material = material.copy(ambient = 1f))
        }

        val w = World.default.copy(objects = listOf(shape))
        val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))

        val i = Intersection(1, shape)
        val comps = i.prepareComputations(r)
        val color = w.reflectedColor(comps, 0)
        assertFloat3Equals(actual = color, expected = Color.Black)
    }

    @JsName("The_reflected_color_for_a_reflective_material")
    @Test
    fun `The reflected color for a reflective material`() {
        val shape = Plane(
            material = Material(
                reflective = 0.5f
            ),
            transform = Matrix.translation(0, -1, 0)
        )
        val w = World.default.copy(
            objects = World.default.objects + shape
        )
        val r = Ray(
            Point(0, 0, -3),
            Vector(0, -SQUARE_ROOT_OF_2_OVER_2, SQUARE_ROOT_OF_2_OVER_2)
        )
        val i = Intersection(SQUARE_ROOT_OF_2, shape)
        val comps = i.prepareComputations(r)
        val color = w.reflectedColor(comps, 5)
        assertFloat3Equals(actual = color, expected = Color(0.19032, 0.2379, 0.14274))
    }

    @JsName("shadeHit_with_a_reflective_material")
    @Test
    fun `shadeHit with a reflective material`() {
        val shape = Plane(
            material = Material(reflective = 0.5),
            transform = Matrix.translation(0, -1, 0)
        )
        val w = World.default.copy(objects = World.default.objects + shape)
        val r = Ray(Point(0, 0, -3), Vector(0, -SQUARE_ROOT_OF_2_OVER_2, SQUARE_ROOT_OF_2_OVER_2))
        val i = Intersection(SQUARE_ROOT_OF_2, shape)
        val comps = i.prepareComputations(r)
        val color = w.shadeHit(comps, 5)
        assertFloat3Equals(actual = color, expected = Color(0.87677, 0.92436, 0.82918))
    }

    @JsName("colorAt_with_mutually_reflective_surfaces")
    @Test
    fun `colorAt with mutually reflective surfaces`() {
        val lower = Plane(
            material = Material(reflective = 1),
            transform = Matrix.translation(0, -1, 0)
        )

        val upper = Plane(
            material = Material(reflective = 1),
            transform = Matrix.translation(0, 1, 0)
        )

        val w = World.default.copy(
            lights = listOf(
                PointLight(Point(0, 0, 0), Color.White)
            ),
            objects = listOf(upper, lower)
        )

        val r = Ray(Point(0, 0, 0), Vector(0, 1, 0))
        // should finish and not stack over flow
        assertNotNull(w.colorAt(r, 10))
    }

    @JsName("The_reflected_color_at_the_maximum_recursive_depth")
    @Test
    fun `The reflected color at the maximum recursive depth`() {
        val shape = Plane(
            material = Material(reflective = 0.5),
            transform = Matrix.translation(0, -1, 0)
        )
        val w = World.default.copy(objects = World.default.objects + shape)
        val r = Ray(Point(0, 0, -3), Vector(0, -SQUARE_ROOT_OF_2_OVER_2, SQUARE_ROOT_OF_2_OVER_2))
        val i = Intersection(SQUARE_ROOT_OF_2, shape)
        val comps = i.prepareComputations(r)
        val color = w.reflectedColor(comps, 0)
        assertFloat3Equals(actual = color, expected = Color.Black)
    }

    @JsName("The_refracted_color_with_an_opaque_surface")
    @Test
    fun `The refracted color with an opaque surface`() {
        val w = World.default
        val shape = w.objects.first()
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val xs = Intersections(Intersection(4, shape), Intersection(6, shape))
        val comps = xs.first().prepareComputations(r, xs)
        val c = w.refractedColor(comps, 5)
        assertEquals(actual = c, expected = Color.Black)
    }

    @JsName("The_refracted_color_at_the_maximum_recursive_depth")
    @Test
    fun `The refracted color at the maximum recursive depth`() {
        val sphere = World.default.objects.first()
            .let { it as Sphere }
            .let { it.copy(material = it.material.copy(transparency = 1f, refractiveIndex = 1.5f)) }

        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val xs = Intersections(Intersection(4, sphere), Intersection(6, sphere))
        val comps = xs.first().prepareComputations(r, xs)
        val c = World.default.refractedColor(comps, 0)
        assertEquals(actual = c, expected = Color.Black)
    }

    @JsName("The_refracted_color_under_total_internal_reflection")
    @Test
    fun `The refracted color under total internal reflection`() {
        val w = World.default
        val shape = w.objects.first()
            .let { it as Sphere }
            .let { it.copy(material = it.material.copy(transparency = 1f, refractiveIndex = 1.5f)) }

        val r = Ray(Point(0, 0, SQUARE_ROOT_OF_2_OVER_2), Vector(0, 1, 0))
        val xs = Intersections(
            Intersection(-SQUARE_ROOT_OF_2_OVER_2, shape),
            Intersection(SQUARE_ROOT_OF_2_OVER_2, shape)
        )
        // This time we are inside the sphere, so we need to look at the second intersection
        val comps = xs.last().prepareComputations(r, xs)
        val c = w.refractedColor(comps, 5)
        assertEquals(actual = c, expected = Color.Black)
    }

    @JsName("The_refracted_color_with_a_refracted_ray")
    @Test
    fun `The refracted color with a refracted ray`() {
        val A = World.default.objects.first()
            .let { it as Sphere }
            .let { it.copy(material = it.material.copy(ambient = 1f, pattern = TestPattern())) }

        val B = World.default.objects.last()
            .let { it as Sphere }
            .let { it.copy(material = it.material.copy(transparency = 1.0f, refractiveIndex = 1.5f)) }

        val w = World.default.copy(objects = listOf(A, B))

        val r = Ray(Point(0, 0, 0.1), Vector(0, 1, 0))
        val xs = Intersections(
            Intersection(-0.9899, A),
            Intersection(-0.4899, B),
            Intersection(0.4899, B),
            Intersection(0.9899, A)
        )
        val comps = xs[2].prepareComputations(r, xs)
        val c = w.refractedColor(comps, 5)
        assertFloat3Equals(actual = c, expected = Color(0, 0.99888, 0.04725))
    }

    @JsName("shadeHit_with_a_transparent_material")
    @Test
    fun `shadeHit with a transparent material`() {
        val floor = Plane(
            transform = Matrix.translation(0, -1, 0),
            material = Material(transparency = 0.5, refractiveIndex = 1.5)
        )

        val ball = Sphere(
            material = Material(
                color = Color(1, 0, 0),
                ambient = 0.5
            ),
            transform = Matrix.translation(0, -3.5, -0.5)
        )

        val w = World.default.let {
            it.copy(objects = it.objects + floor + ball)
        }

        val r = Ray(Point(0, 0, -3), Vector(0, -SQUARE_ROOT_OF_2_OVER_2, SQUARE_ROOT_OF_2_OVER_2))
        val xs = Intersections(Intersection(SQUARE_ROOT_OF_2, floor))
        val comps = xs.first().prepareComputations(r, xs)
        val color = w.shadeHit(comps, 5)
        assertFloat3Equals(actual = color, expected = Color(0.93642, 0.68642, 0.68642))
    }

    @JsName("shadeHit_with_a_reflective_transparent_material")
    @Test
    fun `shadeHit with a reflective transparent material`() {
        val r = Ray(Point(0, 0, -3), Vector(0, -SQUARE_ROOT_OF_2_OVER_2, SQUARE_ROOT_OF_2_OVER_2))
        val floor = Plane(
            transform = Matrix.translation(0, -1, 0),
            material = Material(
                reflective = 0.5,
                transparency = 0.5,
                refractiveIndex = 1.5
            )
        )

        val ball = Sphere(
            material = Material(
                color = Color(1, 0, 0),
                ambient = 0.5
            ),
            transform = Matrix.translation(0, -3.5, -0.5)
        )

        val w = World.default.let {
            it.copy(objects = it.objects + ball + floor)
        }

        val xs = Intersections(Intersection(SQUARE_ROOT_OF_2, floor))
        val comps = xs.first().prepareComputations(r, xs)
        val color = w.shadeHit(comps, 5)
        assertFloat3Equals(actual = color, expected = Color(0.93391, 0.69643, 0.69243))
    }
}
