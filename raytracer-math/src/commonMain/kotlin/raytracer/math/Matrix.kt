@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

import kotlin.math.abs

data class Matrix(
  val data: List<List<Float>>
) {

  val size = data.size

  inline operator fun get(m: Int, n: Int): Float = data[m][n]

  inline operator fun times(other: Matrix): Matrix {
    if (other.size != size) throw Exception("Matrix $other is not the same size as $this")

    val self = this

    val newMatrix = MutableList(size) { m ->
      MutableList(size) { n ->
        var result = 0f
        for (i in data.indices) {
          result += self[m, i] * other[i, n]
        }
        result
      }
    }

    return Matrix(newMatrix)
  }

  inline operator fun times(tuple: Tuple): Tuple {
    val newTuple = Array(4) { 0f }
    data.forEachIndexed { index, m ->
      newTuple[index] = m.toTuple() dot tuple
    }

    return newTuple.toTuple()
  }

  inline fun transpose(): Matrix {
    if (this == IDENTITY) return IDENTITY

    val newMatrix = MutableList(size) { m ->
      MutableList(size) { n ->
        data[n][m]
      }
    }

    return Matrix(newMatrix)
  }

  val determinant: Float by lazy(LazyThreadSafetyMode.NONE) {
    if (size == 2) {
      val a = data[0][0]
      val b = data[0][1]
      val c = data[1][0]
      val d = data[1][1]

      a * d - b * c
    } else {
      var determinant = 0f
      for (m in data.indices) {
        determinant += data[0][m] * cofactor(0, m)
      }
      determinant
    }
  }

  val isInvertible get() = determinant != 0f

  inline fun subMatrixOf(mToDrop: Int, nToDrop: Int): Matrix {
    val newMatrix = mutableListOf<MutableList<Float>>()

    var newMatrixIndex = 0

    data.forEachIndexed m@{ m, row ->
      if (m == mToDrop) return@m
      newMatrix.add(mutableListOf())
      row.forEachIndexed n@{ n, data ->
        if (n == nToDrop) return@n
        newMatrix[newMatrixIndex].add(data)
      }
      newMatrixIndex++
    }

    return Matrix(newMatrix)
  }

  inline fun minor(mToDrop: Int, nToDrop: Int): Float {
    val subMatrix = subMatrixOf(mToDrop, nToDrop)
    return subMatrix.determinant
  }

  inline fun cofactor(mToDrop: Int, nToDrop: Int): Float {
    val minor = minor(mToDrop, nToDrop)
    val isEven = (mToDrop + nToDrop) and 1 == 0
    return if (isEven) minor else -1 * minor
  }

  fun inverse(): Matrix {
    if (!isInvertible) throw Exception("Matrix is not invertible and can not be inverted")

    val M2 = List(size) { m ->
      List(size) { n ->
        val c = cofactor(n, m)
        c / determinant
      }
    }

    return Matrix(M2)
  }

  fun fuzzyEquals(other: Matrix): Boolean {
    for (m in data.indices) {
      for (n in data.indices) {
        if (abs(data[m][n] - other[m, n]) > EPSILON) return false
      }
    }

    return true
  }

  override fun toString(): String =
    data.joinToString(separator = "\n", postfix = "\n", prefix = "\n") { row ->
      row.joinToString(separator = " ", postfix = "|", prefix = "|")
    }

  companion object {
    val IDENTITY = Matrix {
      row(1, 0, 0, 0)
      row(0, 1, 0, 0)
      row(0, 0, 1, 0)
      row(0, 0, 0, 1)
    }

    fun translation(x: Number, y: Number, z: Number) = Matrix {
      row(1, 0, 0, x.toFloat())
      row(0, 1, 0, y.toFloat())
      row(0, 0, 1, z.toFloat())
      row(0, 0, 0, 1)
    }

    fun scaling(x: Number, y: Number, z: Number) = Matrix {
      row(x.toFloat(), 0, 0, 0)
      row(0, y.toFloat(), 0, 0)
      row(0, 0, z.toFloat(), 0)
      row(0, 0, 0, 1)
    }
  }
}

class MatrixBuilder {
  private val columns = mutableListOf<List<Float>>()

  fun row(a1: Number, a2: Number) {
    columns.add(listOf(a1.toFloat(), a2.toFloat()))
  }

  fun row(a1: Number, a2: Number, a3: Number) {
    columns.add(listOf(a1.toFloat(), a2.toFloat(), a3.toFloat()))
  }

  fun row(a1: Number, a2: Number, a3: Number, a4: Number) {
    columns.add(listOf(a1.toFloat(), a2.toFloat(), a3.toFloat(), a4.toFloat()))
  }

  fun toMatrix(): Matrix {
    return Matrix(columns)
  }
}

@Suppress("FunctionName")
fun Matrix(create: MatrixBuilder.() -> Unit): Matrix {
  val builder = MatrixBuilder()
  builder.create()
  return builder.toMatrix()
}
