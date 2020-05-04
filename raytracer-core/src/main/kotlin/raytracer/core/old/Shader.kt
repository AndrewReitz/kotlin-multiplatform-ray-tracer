@file:Suppress("NOTHING_TO_INLINE")

package raytracer.core.old

import raytracer.core.Color
import raytracer.math.old.Vector3
import kotlin.math.pow
import kotlin.math.sqrt

sealed class Shader {
    abstract fun calculateColor(position: Vector3, viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color

    internal inline fun isInShadow(shapes: List<Shape>, lightRay: Ray) = shapes.any { it.isHit(lightRay).isHit }
}
data class BlinnPhong(val values: List<Double>) : Shader() {

    private val lambertion = Lambertion(values)

    override fun calculateColor(position: Vector3, viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color {

        // size must be 8

        val newStart = viewRay.position + (viewRay.direction * t.value)

        val n = (newStart - position).normalize
        val l = (light.position - newStart).normalize

        val lightRay = Ray(position = n, direction = l)

        val inShadow = isInShadow(shapes, lightRay)

        val temp = n dot l
        val bling = if (temp > 0 && !inShadow) temp.pow(values[6]) else 0.0


        val lambertion = lambertion.calculateColor(position, viewRay, light, t, shapes)
        val blinnPhong = Color(
            red = 1.0 * values[3] * bling * light.intensity.x,
            green = 1.0 * values[4] * bling * light.intensity.y,
            blue = 1.0 * values[5] * bling * light.intensity.z
        )

        return lambertion + blinnPhong
    }
}

data class Lambertion(val values: List<Double>) : Shader() {
    override fun calculateColor(position: Vector3, viewRay: Ray, light: Light, t: T, shapes: List<Shape>): Color {
        val newStart = viewRay.position + (viewRay.direction * t.value)
        val v = (viewRay.position - newStart).normalize
        val n = (newStart - position).normalize
        val l = (light.position - newStart).normalize

        val hDivisor = sqrt((v + l) dot (v + l))
        val h = ((v + l) / hDivisor).normalize

        val lightRay = Ray(newStart, l)

        val inShadow = isInShadow(shapes, lightRay)

        val temp = n dot h
        val lambertion = if (temp > 0 && !inShadow) temp.pow(values[6]) else 0.0

        return Color(
            red = 0.05 * values[0] + 1.0 * values[0] * lambertion * light.intensity.x,
            green = 0.05 * values[1] + 1.0 * values[1] * lambertion * light.intensity.y,
            blue = 0.05 * values[2] + 1.0 * values[2] * lambertion * light.intensity.z
        )
    }
}
