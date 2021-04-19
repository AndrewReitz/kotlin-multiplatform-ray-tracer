package raytracer.core

import kotlinx.serialization.Serializable
import raytracer.core.shapes.Shape
import raytracer.core.shapes.Sphere
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.toVector
import kotlin.math.pow
import kotlin.math.sqrt

@Serializable
data class World(
    val lights: List<PointLight> = emptyList(),
    val objects: List<Shape> = emptyList()
) {

    fun intersect(ray: Ray): Intersections = Intersections(
        objects.asSequence()
            .map { it.intersect(ray) }
            .flatten()
            .sortedBy { it.time }
            .toList()
    )

    fun shadeHit(comps: Computation, reflectionsLeft: Int): Color = lights.map {
        val shadowed = isShadowed(comps.overPoint, it)
        val material = comps.obj.material

        val surface = material.lighting(
            obj = comps.obj,
            light = it,
            position = comps.overPoint,
            eyev = comps.eyev,
            normalv = comps.normalv,
            inShadow = shadowed
        )

        val reflected = reflectedColor(comps, reflectionsLeft)
        val refracted = refractedColor(comps, reflectionsLeft)

        if (material.reflective > 0 && material.transparency > 0) {
            val reflectance = comps.schlick
            surface + reflected * reflectance + refracted * (1 - reflectance)
        } else {
            surface + reflected + refracted
        }
    }.reduce { sum, color ->
        sum + color
    }

    fun colorAt(ray: Ray, reflectionsLeft: Int): Color {
        val intersections = intersect(ray)
        val hit = intersections.hit() ?: return Color.Black
        val comps = hit.prepareComputations(ray, intersections)
        return shadeHit(comps, reflectionsLeft)
    }

    fun isShadowed(point: Point, light: PointLight): Boolean {
        val v = (light.position - point).toVector()
        val distance = v.magnitude
        val direction = v.normalize()

        val r = Ray(origin = point, direction = direction)
        val intersections = intersect(r)

        val h = intersections.hit()
        return h != null && h.time < distance
    }

    fun reflectedColor(comps: Computation, reflectionsLeft: Int): Color {
        if (comps.obj.material.reflective == 0f || reflectionsLeft < 1) {
            return Color.Black
        }

        val reflectRay = Ray(comps.overPoint, comps.reflectv)
        val color = colorAt(reflectRay, reflectionsLeft - 1)

        return color * comps.obj.material.reflective
    }

    fun refractedColor(comps: Computation, refractionLevel: Int): Color {
        if (comps.obj.material.transparency == 0f) {
            return Color.Black
        }

        if (refractionLevel == 0) {
            return Color.Black
        }

        // Find the ratio of the first index of refraction to the second.
        // this is inverted definition of Snell's Law.
        val nRatio = comps.n1 / comps.n2

        // Cos of theta_i is the same as the dot product of the two vectors
        val cosI = comps.eyev dot comps.normalv

        // find Sin of theta_t squared via trig identity
        val sin2T = nRatio.pow(2) * (1 - cosI.pow(2))

        // if sin of theta t squared is greater than 1 we have total internal reflection
        if (sin2T > 1) {
            return Color.Black
        }

        // cos theta t via trig identity
        val cosT = sqrt(1.0 - sin2T)

        // compute the direction of the refracted ray
        val direction = comps.normalv * (nRatio * cosI - cosT) - comps.eyev * nRatio

        // create the refracted ray
        val refractRay = Ray(comps.underPoint, direction)

        // find the color of the refracted ray, making sure to multiply by the
        // transparency value to account for any opacity
        return colorAt(refractRay, refractionLevel - 1) * comps.obj.material.transparency
    }

    companion object {
        val default
            get() = World(
                lights = listOf(
                    PointLight(
                        position = Point(-10, 10, -10),
                        intensity = Color(1, 1, 1)
                    )
                ),
                objects = listOf(
                    Sphere(
                        material = Material(
                            color = Color(
                                red = 0.8,
                                green = 1.0,
                                blue = 0.6
                            ),
                            diffuse = 0.7f,
                            specular = 0.2f
                        )
                    ),
                    Sphere(
                        transform = Matrix.scaling(0.5, 0.5, 0.5)
                    )
                )
            )
    }
}
