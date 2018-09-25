package raytracer.core

import raytracer.math.Vector3

interface Camera {
    fun getRay(v: Double, u: Double): Ray
}

class PerspecticveCamera(
        val position: Vector3,
        val viewDirection: Vector3,
        val focalLength: Double,
        val imagePlaneWidth: Double,
        val height: Double,
        val width: Double
) : Camera {

    val w: Vector3
    val u: Vector3
    val v: Vector3


    init {
        val tempW = !viewDirection / Math.sqrt(viewDirection `∙` viewDirection)

        val b = Vector3(x = 0.0, y = 1.0, z = 0.0)

        val tempU = (b `×` tempW)
                .let { it `∙` it }
                .let { Math.sqrt(it) }
                .let { b / it }

        v = tempW.crossProduct(tempU).normalize
        u = tempU.normalize
        w = tempW.normalize
    }

    override fun getRay(v: Double, u: Double): Ray {
        val tempW = w * focalLength
        val tempU = this.u * u
        val tempV = this.v * v

        val negativeW = !tempW + tempU + tempV

        return Ray(position = position, direction = negativeW)
    }
}
