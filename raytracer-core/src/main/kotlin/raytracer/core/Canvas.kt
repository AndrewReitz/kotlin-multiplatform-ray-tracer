package raytracer.core

data class Canvas(
  val width: Int,
  val height: Int
) {
  val pixels: Array<Array<Color>> = Array(width) {
    Array(height) { Color() }
  }

  operator fun set(x: Int, y: Int, color: Color) {
    pixels[x][y] = color
  }

  operator fun get(x: Int, y: Int) = pixels[x][y]
}
