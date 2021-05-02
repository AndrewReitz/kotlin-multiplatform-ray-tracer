package raytracer.core.shapes

import kotlinx.serialization.Serializable
import raytracer.core.Intersection
import raytracer.core.Intersections
import raytracer.core.Material
import raytracer.core.Ray
import raytracer.math.EPSILON
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import raytracer.math.Vector3
import kotlin.math.abs

@Serializable
data class Plane(
    override val transform: Matrix = Matrix.IDENTITY,
    override val material: Material = Material()
) : Shape {

    override fun localIntersect(localRay: Ray): Intersections {
        if (abs(localRay.direction.y) < EPSILON) {
            return Intersections.EMPTY
        }

        val t = -localRay.origin.y / localRay.direction.y
        return Intersections(Intersection(t, this))
    }

    override fun localNormalAt(localPoint: Point): Vector3 = Vector(0, 1, 0)
}
