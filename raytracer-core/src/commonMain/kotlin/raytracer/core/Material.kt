package raytracer.core

import raytracer.math.Point
import raytracer.math.Vector3
import raytracer.math.toVector
import kotlin.math.pow

data class Material(
  val ambient: Float = 0.1f,
  val diffuse: Float = 0.9f,
  val specular: Float = 0.9f,
  val shininess: Float = 200f,
  val color: Color = Color(1f, 1f, 1f)
) {
  init {
    require(ambient in 0.0..1.0) { "ambient: must be between 0 and 1 was $ambient" }
    require(diffuse in 0.0..1.0) { "diffuse: must be between 0 and 1 was $diffuse" }
    require(specular in 0.0..1.0) { "specular: must be between 0 and 1 was $specular" }
    require(shininess >= 0) { "shininess: 0 or larger was $shininess" }
  }

  fun lighting(light: PointLight, position: Point, eyev: Vector3, normalv: Vector3): Color {
    // combine the surface color with the light's color/intensity
    val effectiveColor = color * light.intensity

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
    val diffuse = effectiveColor * diffuse * lightDotNormal

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
    val specular = light.intensity * specular * factor
    return ambient + diffuse + specular
  }
}
