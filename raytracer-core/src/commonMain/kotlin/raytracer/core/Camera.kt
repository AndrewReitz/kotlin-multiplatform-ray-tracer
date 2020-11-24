package raytracer.core

import kotlinx.coroutines.*
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.toVector
import kotlin.math.tan

class Camera(
    val hsize: Int,
    val vsize: Int,
    val fieldOfView: Float,
    val transform: Matrix = Matrix.IDENTITY
) {
    val pixelSize: Float

    private val halfWidth: Float
    private val halfHeight: Float

    constructor(hsize: Number, vsize: Number, fieldOfView: Number, transform: Matrix = Matrix.IDENTITY) : this(
        hsize = hsize.toInt(), vsize = vsize.toInt(), fieldOfView = fieldOfView.toFloat(), transform = transform
    )

    init {
        val halfView = tan(fieldOfView / 2)
        val aspect = hsize.toDouble() / vsize.toDouble()

        val (tempHalfWidth, tempHalfHeight) = if (aspect >= 1) {
            halfView.toDouble() to halfView / aspect
        } else {
            halfView * aspect to halfView.toDouble()
        }

        pixelSize = ((tempHalfWidth * 2.0) / hsize).toFloat()
        halfWidth = tempHalfWidth.toFloat()
        halfHeight = tempHalfHeight.toFloat()

    }

    fun rayForPixel(px: Int, py: Int): Ray {
        // the offset from the edge of the canvas to the pixel's center
        val xoffset = (px + 0.5) * pixelSize
        val yoffset = (py + 0.5) * pixelSize

        // the untransformed coordinates of the pixel in world space.
        // (remember that the camera looks toward -z, so +x is to the *left*.)
        val worldX = halfWidth - xoffset
        val worldY = halfHeight - yoffset

        // using the camera matrix, transform the canvas point and the origin,
        // and then compute the ray's direction vector.
        // (remember that the canvas is at z=-1)
        val inverseTransform = transform.inverse()
        val pixel = inverseTransform * Point(worldX, worldY, -1)
        val origin = inverseTransform * Point(0, 0, 0)
        val direction = (pixel - origin).normalize().toVector()

        return Ray(origin, direction)
    }

    suspend fun render(world: World): Canvas = coroutineScope {
        val jobs = mutableListOf<Job>()
        val image = Canvas(hsize, vsize)
        for (y in 0 until vsize) {
            for (x in 0 until hsize) {
                jobs += launch(Dispatchers.Default) {
                    val ray = rayForPixel(x, y)
                    val color = world.colorAt(ray)
                    image[x, y] = color
                }
            }
        }

        jobs.joinAll()

        image
    }

    override fun toString(): String {
        return "Camera(hsize=$hsize, " +
                "vsize=$vsize, " +
                "fieldOfView=$fieldOfView, " +
                "transform=$transform, " +
                "halfWidth=$halfWidth, " +
                "halfHeight=$halfHeight, " +
                "pixelSize=$pixelSize)"
    }
}