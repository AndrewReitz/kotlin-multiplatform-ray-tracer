package raytracer.console

import raytracer.math.book.Point
import raytracer.math.book.Tuple
import raytracer.math.book.Vector

fun main() {
  var p = Projectile(
    position = Point(0, 1, 0),
    velocity = Vector(1, 1, 0).normalize()
  )

  val e = Environment(
    wind = Vector(0, -0.1, 0),
    gravity = Vector(-0.01, 0, 0)
  )
  var count = 0;
  while (p.position.y >= 0) {
    p = tick(p, e)
    println("tick = ${++count} postion = $p")
  }
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
