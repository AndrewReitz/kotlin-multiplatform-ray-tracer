package raytracer.core

import raytracer.core.patterns.Pattern
import raytracer.core.shapes.Shape
import raytracer.core.shapes.Sphere
import raytracer.math.Point
import raytracer.math.Vector3
import raytracer.math.toVector
import kotlin.math.pow

/**
 * Common Refractive Index
 * Vacuum: 1
 * Air: 1.00029
 * Water: 1.333
 * Glass: 1.52
 * Diamond: 2.417
 */
fun Material(
    ambient: Number = 0.1f,
    diffuse: Number = 0.9f,
    specular: Number = 0.9f,
    shininess: Number = 200f,
    color: Color = Color.White,
    pattern: Pattern? = null,
    reflective: Number = 0,
    transparency: Number = 0,
    refractiveIndex: Number = 1
) = Material(
    ambient = ambient.toFloat(),
    diffuse = diffuse.toFloat(),
    specular = specular.toFloat(),
    shininess = shininess.toFloat(),
    color = color,
    pattern = pattern,
    reflective = reflective.toFloat(),
    transparency = transparency.toFloat(),
    refractiveIndex = refractiveIndex.toFloat()
)

data class Material(
    val ambient: Float,
    val diffuse: Float,
    val specular: Float,
    val shininess: Float,
    val color: Color,
    val pattern: Pattern?,
    val reflective: Float,
    val transparency: Float,
    val refractiveIndex: Float
) {

    init {
        require(ambient in 0.0..1.0) { "ambient: must be between 0 and 1 was $ambient" }
        require(diffuse in 0.0..1.0) { "diffuse: must be between 0 and 1 was $diffuse" }
        require(specular in 0.0..1.0) { "specular: must be between 0 and 1 was $specular" }
        require(shininess >= 0) { "shininess: 0 or larger was $shininess" }
        require(reflective in 0.0..1.0) { "reflective: must be between 0 and 1 was $reflective" }
        require(transparency in 0.0..1.0) { "transparency: must be between 0 and 1 was $transparency" }
    }

    fun lighting(
        light: PointLight,
        position: Point,
        eyev: Vector3,
        normalv: Vector3,
        inShadow: Boolean = false,
        obj: Shape = Sphere(),
    ): Color {
        // combine the surface color with the light's color/intensity
        val actualColor = pattern?.patternAtShape(obj, position) ?: color
        val effectiveColor = actualColor * light.intensity

        // find the direction to the light source
        val lightv = (light.position - position).toVector().normalize()

        // compute the ambient contribution
        val ambient = effectiveColor * this.ambient

        // cosine of the angle between the lgith vector and the normal vector.
        // A negative number means the light is on the other side of the surface.
        val lightDotNormal = lightv dot normalv
        if (lightDotNormal < 0) {
            val diffuse = Color.Black
            val specular = Color.Black
            return ambient + diffuse + specular
        }

        // compute the diffuse contribution
        val diffuse = if (inShadow) Color.Black else effectiveColor * diffuse * lightDotNormal

        // represents the cosine of the angle between the reflection vector
        // and the eye vector. A negative number means the light reflects away
        // from the eye.
        val reflectv = -lightv reflect normalv
        val reflectDotEye = reflectv dot eyev

        if (reflectDotEye <= 0) {
            val specular = Color.Black
            return ambient + diffuse + specular
        }

        // compute the specular contribution
        val factor = reflectDotEye.pow(shininess)
        val specular = if (inShadow) Color.Black else light.intensity * specular * factor
        return ambient + diffuse + specular
    }
}
