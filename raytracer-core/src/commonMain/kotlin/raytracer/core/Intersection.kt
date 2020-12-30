package raytracer.core

import raytracer.core.shapes.Shape

data class Intersection(val time: Float, val obj: Shape) {
    fun prepareComputations(ray: Ray): Computation {
        val point = ray.position(time)
        val normal = obj.normalAt(point)
        val eye = -ray.direction
        val inside = normal dot eye < 0
        val normalv = if (inside) -normal else normal
        val reflectv = ray.direction reflect normalv

        return Computation(
            time = time,
            obj = obj,
            point = point,
            eyev = -ray.direction,
            normalv = normalv,
            inside = inside,
            reflectv = reflectv
        )
    }
}

fun Intersection(time: Number, obj: Shape) = Intersection(time.toFloat(), obj)
