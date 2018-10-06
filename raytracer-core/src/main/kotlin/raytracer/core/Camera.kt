package raytracer.core

import raytracer.math.Vector3

interface Camera {
    val imagePlaneWidth: Double

    fun getRay(v: Double, u: Double): Ray
}

class PerspecticveCamera(
    viewDirection: Vector3,
    private val position: Vector3,
    private val focalLength: Double,
    override val imagePlaneWidth: Double,
    val height: Double,
    val width: Double
) : Camera {

    private val w: Vector3
    private val u: Vector3
    private val v: Vector3

    init {
        val tempW = !viewDirection / Math.sqrt(viewDirection dot viewDirection)

        val b = Vector3(x = 0.0, y = 1.0, z = 0.0)

        val tempU = (b cross tempW)
                .let { it dot it }
                .let { Math.sqrt(it) }
                .let { b / it }

        v = tempW.cross(tempU).normalize
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
