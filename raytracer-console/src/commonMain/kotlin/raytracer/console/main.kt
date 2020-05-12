package raytracer.console

import raytracer.core.Canvas
import raytracer.core.Color
import raytracer.math.Matrix
import raytracer.math.Point
import kotlin.math.PI
import kotlin.math.roundToInt

fun main() {
  val color = Color(1, 1, 1)
  val c = Canvas(500, 500)
  val canvasCenter = Point(250, 250, 0)
  val clockRadius = (250 * 0.75).roundToInt()
  val centerPoint = Point(0, 0, 0)

  val translation = Matrix.translation(0, 1, 0)
  val rotation = Matrix.rotationZ(PI / 6)
  var p = translation * centerPoint

  for(i in 0 until 12) {
    p = rotation * p
    val x = p.x * clockRadius + canvasCenter.x
    val y = p.y * clockRadius + canvasCenter.y

    c[x.roundToInt(), y.roundToInt()] = color
  }

  writeToFile(c.toPpm(), "image.ppm")
}
