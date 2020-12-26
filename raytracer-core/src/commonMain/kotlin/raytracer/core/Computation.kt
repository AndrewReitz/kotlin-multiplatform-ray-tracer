package raytracer.core

import raytracer.core.shapes.Shape
import raytracer.math.EPSILON
import raytracer.math.Point
import raytracer.math.Vector3

data class Computation(
    val time: Float,
    val obj: Shape,
    val point: Point,
    val eyev: Vector3,
    val normalv: Vector3,
    val inside: Boolean,
    val reflectv: Vector3
) {
    val overPoint by lazy { point + normalv * EPSILON }
}
