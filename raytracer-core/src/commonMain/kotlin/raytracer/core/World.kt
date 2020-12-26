package raytracer.core

import raytracer.core.shapes.Shape
import raytracer.core.shapes.Sphere
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.toVector

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
        comps.obj.material.lighting(
            obj = comps.obj,
            light = it,
            position = comps.overPoint,
            eyev = comps.eyev,
            normalv = comps.normalv,
            inShadow = isShadowed(comps.overPoint, it)
        ) + reflectedColor(comps, reflectionsLeft)
    }.reduce { sum, color ->
        sum + color
    }

    fun colorAt(ray: Ray, reflectionsLeft: Int): Color {
        val hit = intersect(ray).hit() ?: return Color.Black
        val comps = hit.prepareComputations(ray)
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
