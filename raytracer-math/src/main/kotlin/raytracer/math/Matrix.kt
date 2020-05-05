@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

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
        0f
      }
    }

    // this can be optomized if done in specific
    for (m in data.indices) {
      for (n in data.indices) {
        var result = 0f
        for (i in data.indices) {
          result += self[n, i] * other[i, m]
        }
        newMatrix[n][m] = result
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

  fun transpose(): Matrix {
    if (this == IDENTITY_MATRIX) return IDENTITY_MATRIX

    val newMatrix = MutableList(size) { m ->
      MutableList(size) { n ->
        data[n][m]
      }
    }

    return Matrix(newMatrix)
  }

  override fun toString(): String =
    data.joinToString(separator = "\n", postfix = "\n", prefix = "\n") { row ->
      row.joinToString(separator = " ", postfix = "|", prefix = "|")
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

fun Matrix(create: MatrixBuilder.() -> Unit): Matrix {
  val builder = MatrixBuilder()
  builder.create()
  return builder.toMatrix()
}
