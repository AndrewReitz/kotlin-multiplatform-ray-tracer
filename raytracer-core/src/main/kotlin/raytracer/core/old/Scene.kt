package raytracer.core.old

data class Scene(
  val height: Int,
  val width: Int,
  val camera: Camera,
  val lights: List<Light> = emptyList(),
  val shapes: List<Shape> = emptyList()
)