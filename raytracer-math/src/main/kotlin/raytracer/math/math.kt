package raytracer.math

const val EPSILON = 0.00001
const val ONE_OVER_EPSILON = 1 / EPSILON

val IDENTITY_MATRIX = Matrix {
  row(1, 0, 0, 0)
  row(0, 1, 0, 0)
  row(0, 0, 1, 0)
  row(0, 0, 0, 1)
}

