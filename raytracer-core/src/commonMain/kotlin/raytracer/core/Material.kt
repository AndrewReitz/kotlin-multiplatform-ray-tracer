package raytracer.core

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
}
