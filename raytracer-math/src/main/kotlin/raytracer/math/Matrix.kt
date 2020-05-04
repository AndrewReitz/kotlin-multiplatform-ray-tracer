@file:Suppress("NOTHING_TO_INLINE")

package raytracer.math

data class Matrix(
  val columns: List<List<Float>>
) {

  val size = columns.size

  inline operator fun get(x: Int, y: Int): Float = columns[x][y]

  inline operator fun times(other: Matrix): Matrix {
    if (other.size != size) throw Exception("Matrix $other is not the same size as $this")

    val self = this

    val newMatrix = MutableList(size) {
      MutableList(size) {
        0f
      }
    }

    // this can be optomized if done in specific
    for (c in columns.indices) {
      for (r in columns.indices) {
        var result = 0f
        for (i in columns.indices) {
          result += self[r, i] * other[i, c]
        }
        newMatrix[r][c] = result
      }
    }

    return Matrix(newMatrix)
  }

  inline operator fun times(tuple: Tuple): Tuple {
    val newTuple = Array(4) { 0f }
    columns.forEachIndexed { index, c ->
      newTuple[index] = c.toTuple() dot tuple
    }

    return newTuple.toTuple()
  }

  override fun toString(): String {
    return columns.map { row ->
      row.joinToString(postfix = "]", prefix = "[")
    }.joinToString(separator = "\n", postfix = "]", prefix = "[")
  }

  fun List<Float>.toTuple(): Tuple = Tuple(get(0), get(1), get(2), get(3))
  fun Array<Float>.toTuple(): Tuple = Tuple(get(0), get(1), get(2), get(3))
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
