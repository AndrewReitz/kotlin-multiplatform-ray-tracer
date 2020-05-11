package raytracer.console

import raytracer.core.Canvas
import raytracer.core.Color
import raytracer.math.Point
import raytracer.math.Tuple
import raytracer.math.Vector
import kotlin.math.roundToInt

fun main() {
  var p = Projectile(
    position = Point(0, 1, 0),
    velocity = Vector(1, 1.8, 0).normalize() * 11.25
  )

  val e = Environment(
    wind = Vector(0, -0.1, 0),
    gravity = Vector(-0.01, 0, 0)
  )

  val color = Color(1, 0, 0)
  val c = Canvas(900, 550)

  var count = 0;
  while (p.position.y >= 0) {
    p = tick(p, e)
    println("tick = ${++count} postion = $p")
    c[p.position.x.roundToInt(), c.height - p.position.y.roundToInt()] = color
  }

  writeToFile(c.toPpm(), "image.ppm")
}

fun tick(proj: Projectile, env: Environment): Projectile {
  val position = proj.position + proj.velocity
  val velocity = proj.velocity + env.gravity + env.wind

  return Projectile(position, velocity)
}

data class Environment(
  val wind: Tuple,
  val gravity: Tuple
)

data class Projectile(
  val position: Tuple,
  val velocity: Tuple
)
