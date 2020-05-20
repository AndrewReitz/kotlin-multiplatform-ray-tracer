package raytracer.console

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import raytracer.core.Canvas
import raytracer.core.Color
import raytracer.core.Material
import raytracer.core.PointLight
import raytracer.core.Ray
import raytracer.core.Sphere
import raytracer.math.Point
import raytracer.math.toVector
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main(): Unit = run {
  println("starting...")

  val canvasSize = 1000
  val c = Canvas(canvasSize, canvasSize)

  val rayOrigin = Point(0, 0, -5)
  val wallZ = 10f
  val wallSize = 7f
  val pixelSize = wallSize / canvasSize
  val half = wallSize / 2f

  val shape = Sphere(material = Material(
    color = Color(1, 0.2, 1)
  ))

  val light = PointLight(
    position = Point(-10, 10, -10),
    intensity = Color(1, 1, 1)
  )

  val time = measureTime {
    val jobs = mutableListOf<Job>()

    for (y in 0 until c.width) {
      val worldY = half - pixelSize * y
      for (x in 0 until c.height) {
        println("calculating pixel [$x, $y]")
        jobs += launch(Dispatchers.Default) {
          val worldX = -half + pixelSize * x
          val position = Point(worldX, worldY, wallZ)
          val r = Ray(rayOrigin, (position - rayOrigin).normalize().toVector())
          val xs = r.intersects(shape)

          xs.hit()?.let {hit ->
            val point = r.position(hit.time)
            val normal = hit.obj.normalAt(point)
            val eye = -r.direction
            val color = hit.obj.material.lighting(light, point, eye, normal)
            c[x, y] = color
          }
          println("finished calculating pixel [$x, $y]")
        }
      }
    }

    jobs.forEach { it.join() }
  }

  println("Took ${time.inSeconds} seconds")

  val ppm = measureTime {
    writeToFile(c.toPpm(), "image.ppm")
  }

  println("PPM Took ${ppm.inSeconds} seconds")
}
