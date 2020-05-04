@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math.old

// Note: may need custom equals methods for floating point see
// Chapter 1: Comparing Floating Point Numbers
interface Tuple {
  val x: Float
  val y: Float
  val z: Float
  val w: Float
}

data class Point(
  override val x: Float,
  override val y: Float,
  override val z: Float
) : Tuple {
  override val w: Float = 1.0F

  constructor(x: Int, y: Int, z: Int) : this(x.toFloat(), y.toFloat(), z.toFloat())

  inline operator fun plus(vector: Vector): Point = Point(
    x = x + vector.x,
    y = y + vector.y,
    z = z + vector.z
  )

  inline operator fun minus(point: Point): Vector = Vector(
    x = x - point.x,
    y = y - point.y,
    z = z - point.z
  )

  inline operator fun minus(vector: Vector): Point = Point(
    x = x - vector.x,
    y = y - vector.y,
    z = z - vector.z
  )

  inline operator fun not() = Point(
    x = -x,
    y = -y,
    z = -z
  )
}

data class Vector(
  override val x: Float,
  override val y: Float,
  override val z: Float
) : Tuple {
  override val w: Float = 0F

  constructor(x: Int, y: Int, z: Int) : this(x.toFloat(), y.toFloat(), z.toFloat())

  inline operator fun plus(vector: Vector): Vector = Vector(
    x = x + vector.x,
    y = y + vector.y,
    z = z + vector.z
  )

  inline operator fun plus(point: Point): Point = Point(
    x = x + point.x,
    y = y + point.y,
    z = z + point.z
  )

  inline operator fun minus(vector: Vector) = Vector(
    x = x - vector.x,
    y = y - vector.y,
    z = z - vector.z
  )

  inline operator fun not() = Vector(
    x = -x,
    y = -y,
    z = -z
  )
}
