package raytracer.core

import raytracer.core.shapes.Shape
import raytracer.math.EPSILON
import raytracer.math.Point
import raytracer.math.Vector3
import kotlin.math.pow
import kotlin.math.sqrt

data class Computation(
    val time: Float,
    val obj: Shape,
    val point: Point,
    val eyev: Vector3,
    val normalv: Vector3,
    val inside: Boolean,
    val reflectv: Vector3,

    /** refractive indices of the ray entering object */
    val n1: Float,
    /** refractive indices of the ray exiting the object */
    val n2: Float,
) {
    /**
     * offset over intersection object for a shadow so objects and shadows don't blend
     */
    val overPoint by lazy { point + normalv * EPSILON }

    /**
     * this is where refracted rays originate, move it slightly below the surface
     * so that refraction doesn't blend with the object's surface.
     */
    val underPoint by lazy { point - normalv * EPSILON }

    /**
     * reflectance value based off the schlick approximation formula
     */
    val schlick: Float by lazy {
        // find the cosine of the angle between the eye and normal vectors
        var cos = eyev dot normalv

        // total internal reflection can only occur if n1 > n2
        if (n1 > n2) {
            val n = n1 / n2
            val sin2T = n.pow(2) * (1f - cos.pow(2))
            if (sin2T > 1.0) {
                return@lazy 1f
            }

            // compute cosine of theta t using trig identity
            // when n1 > n2, use cos(theta t) instead
            cos = sqrt(1.0 - sin2T).toFloat()
        }

        val r0 = ((n1 - n2) / (n1 + n2)).pow(2)
        r0 + (1 - r0) * (1 - cos).pow(5)
    }
}
