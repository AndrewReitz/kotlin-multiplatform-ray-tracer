package raytracer.core.shapes

import raytracer.core.Intersection
import raytracer.core.Intersections
import raytracer.core.Material
import raytracer.core.Ray
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector3
import raytracer.math.toVector
import kotlin.math.sqrt

data class Sphere(
    val center: Point = Point(0, 0, 0),
    override val transform: Matrix = Matrix.IDENTITY,
    override val material: Material = Material()
) : Shape {

    override fun localIntersect(localRay: Ray): Intersections {
        val (origin, direction) = localRay
        val sphereToRay = origin - center

        val a = direction dot direction
        val b = 2 * (direction dot sphereToRay)
        val c = (sphereToRay dot sphereToRay) - 1f

        val discriminant = b * b - 4f * a * c

        if (discriminant < 0f) return Intersections.EMPTY

        val t1 = (-b - sqrt(discriminant)) / (2f * a)
        val t2 = (-b + sqrt(discriminant)) / (2f * a)

        return Intersections(Intersection(t1, this), Intersection(t2, this))
    }

    override fun localNormalAt(localPoint: Point): Vector3 {
        return (localPoint - Point(0, 0, 0)).toVector()
    }
}
