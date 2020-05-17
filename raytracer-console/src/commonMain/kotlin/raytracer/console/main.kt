package raytracer.console

import raytracer.core.Canvas
import raytracer.core.Color
import raytracer.core.Ray
import raytracer.core.Sphere
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.toVector
import kotlin.math.PI
import kotlin.math.roundToInt

fun main() {
  val canvasSize = 1000
  val wallColor = Color(1, 0, 0)
  val c = Canvas(canvasSize, canvasSize)

  val rayOrigin = Point(0, 0, -5)
  val wallZ = 10f
  val wallSize = 7f
  val pixelSize = wallSize / canvasSize
  val half = wallSize / 2f

  val shape = Sphere()

  for (y in 0 until c.width) {
    val worldY = half - pixelSize * y
    for (x in 0 until c.height) {
      val worldX = -half + pixelSize * x
      val position = Point(worldX, worldY, wallZ)
      val r = Ray(rayOrigin, (position - rayOrigin).normalize().toVector())
      val xs = r.intersects(shape)

      if (xs.hit() != null) {
        c[x, y] = wallColor
      }
    }
  }

  writeToFile(c.toPpm(), "image.ppm")
}
