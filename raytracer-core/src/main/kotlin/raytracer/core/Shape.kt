package raytracer.core

import raytracer.math.old.Vector3
import kotlin.math.sqrt

interface Shape {
    val shader: Shader
    fun isHit(ray: Ray, t: T = T.default): HitData
    fun getN(start: Vector3): Vector3
    fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color
}

class Sphere(
  private val radius: Double,
  private val position: Vector3,
  override val shader: Shader
) : Shape {

    override fun isHit(ray: Ray, t: T): HitData {
        val vectorDiff = ray.position - position
        val A = ray.direction dot ray.direction
        val B = ray.direction dot vectorDiff
        val C = (vectorDiff dot vectorDiff) - (radius * radius)

        val D = B * B - A * C

        if (D < 0) {
            return HitData(t = t, isHit = false)
        }

        val t0 = (-B - sqrt(D)) / A
        val t1 = (-B + sqrt(D)) / A

        return when {
            t0 > 0.0000001f && t0 < t.value -> HitData(t = T(t0), isHit = true)
            t1 > 0.0000001f && t1 < t.value -> HitData(t = T(t1), isHit = true)
            else -> HitData(t = t, isHit = false)
        }
    }

    override fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color =
            shader.calculateColor(
                    position = position,
                    viewRay = viewRay,
                    light = light,
                    t = t,
                    shapes = shapes
            )

    override fun getN(start: Vector3) = (start - position).normalize
}

class Triangle(
  private val p1: Vector3,
  private val p2: Vector3,
  private val p3: Vector3,
  override val shader: Shader
) : Shape {

    private val n = (p2 - p1)
            .let { it cross (p3 - p1) }
            .apply { normalize }

    override fun isHit(ray: Ray, t: T): HitData {
        val a = p1.x - p2.x
        val b = p1.y - p2.y
        val c = p1.z - p2.z

        val d = p1.x - p3.x
        val e = p1.y - p3.y
        val f = p1.z - p3.z

        val (g, h, i) = ray.direction

        val j = p1.x - ray.position.x
        val k = p1.y - ray.position.y
        val l = p1.z - ray.position.z

        val M = a * (e * i - h * f) + b * (g * f - d * i) + c * (d * h - e * g)

        val beta = (j * (e * i - h * f) + k * (g * f - d * i) + l * (d * h - e * g)) / M
        val alpha = (i * (a * k - j * b) + h * (j * c - a * l) + g * (b * l - k * c)) / M
        val t0 = -1.0 * (f * (a * k - j * b) + e * (j * c - a * l) + d * (b * l - k * c)) / M

        return when {
            alpha < 0 || alpha > 1 -> HitData(t = t, isHit = false)
            beta < 0 || beta > 1 - alpha -> HitData(t = t, isHit = false)
            t0 > 0.0000001 && t0 <= t.value -> HitData(t = T(t0), isHit = true)
            else -> HitData(t = t, isHit = false)
        }
    }

    override fun getN(start: Vector3): Vector3 = n

    override fun calculateColor(viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class HitData(
    val t: T,
    val isHit: Boolean
)
