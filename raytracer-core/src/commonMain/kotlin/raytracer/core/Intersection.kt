package raytracer.core

import raytracer.core.shapes.Shape

data class Intersection(val time: Float, val obj: Shape) {
    fun prepareComputations(ray: Ray, intersections: Intersections = Intersections.EMPTY): Computation {
        val point = ray.position(time)
        val normal = obj.normalAt(point)
        val eye = -ray.direction
        val inside = normal dot eye < 0
        val normalv = if (inside) -normal else normal
        val reflectv = ray.direction reflect normalv

        // list of objects encountered but not yet exited.
        val containers = linkedSetOf<Shape>()
        val hit = this

        // refraction value of object passing from
        var n1 = 0f

        // refraction value of object passing to
        var n2 = 0f

        for (it in intersections) {
            // if this is the hit set refraction to the last object encountered, if there is nothing
            // the set the value to 1
            if (it == hit) {
                n1 = containers.lastOrNull()?.material?.refractiveIndex ?: 1f
            }

            // if the intersection's object is already in the list we
            // are exiting the object, and it should be removed. Otherwise
            // we are entering the object and it should be added.
            if (!containers.remove(it.obj)) {
                containers.add(it.obj)
            }

            if (it == hit) {
                n2 = containers.lastOrNull()?.material?.refractiveIndex ?: 1f
                break
            }
        }

        return Computation(
            time = time,
            obj = obj,
            point = point,
            eyev = -ray.direction,
            normalv = normalv,
            inside = inside,
            reflectv = reflectv,
            n1 = n1,
            n2 = n2
        )
    }
}

fun Intersection(time: Number, obj: Shape) = Intersection(time.toFloat(), obj)
