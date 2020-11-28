package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector3

data class Ray(
    val origin: Point,
    val direction: Vector3
) {
    fun position(time: Number): Point = origin + direction * time.toFloat()

    fun transform(transform: Matrix): Ray = Ray(
        origin = transform * origin,
        direction = transform * direction
    )
}
