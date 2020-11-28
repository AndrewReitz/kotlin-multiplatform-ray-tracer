package raytracer.core

import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector3

interface Shape {
    val transform: Matrix
    val material: Material

    fun normalAt(point: Point): Vector3 {
        val localPoint  = transform.inverse() * point
        val localNormal = localNormalAt(localPoint)
        val worldNormal = transform.inverse().transpose() * localNormal
        return worldNormal.normalize()
    }

    /**
     * Intersect function but transformed into local space.
     */
    fun localIntersect(localRay: Ray): Intersections

    /** Calculate the normal in the shape's local coordinates. */
    fun localNormalAt(localPoint: Point): Vector3

    fun intersect(ray: Ray): Intersections {
        val localRay =  ray.transform(transform.inverse())
        return localIntersect(localRay)
    }
}