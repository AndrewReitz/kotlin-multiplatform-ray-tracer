package raytracer.math

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class Matrix(
    val data: Array<Array<Float>>
) {

    val size = data.size

    operator fun get(m: Int, n: Int): Float = data[m][n]

    operator fun times(other: Matrix): Matrix {
        if (other.size != size) throw Exception("Matrix $other is not the same size as $this")

        val self = this

        val newMatrix = Array(size) { m ->
            Array(size) { n ->
                var result = 0f
                for (i in data.indices) {
                    result += self[m, i] * other[i, n]
                }
                result
            }
        }

        return Matrix(newMatrix)
    }

    operator fun times(point: Point): Point {
        val newTuple = Array(4) { 0f }
        data.forEachIndexed { index, m ->
            newTuple[index] = m.toVector4() dot point
        }

        return newTuple.toPoint()
    }

    operator fun times(vector3: Vector3): Vector3 {
        val newTuple = Array(4) { 0f }
        data.forEachIndexed { index, m ->
            newTuple[index] = m.toVector4() dot vector3
        }

        return newTuple.toVector3()
    }

    operator fun times(vector4: Vector4): Vector4 {
        val newTuple = Array(4) { 0f }
        data.forEachIndexed { index, m ->
            newTuple[index] = m.toVector4() dot vector4
        }

        return newTuple.toVector4()
    }

    fun transpose(): Matrix {
        if (this == IDENTITY) return IDENTITY

        val newMatrix = Array(size) { m ->
            Array(size) { n ->
                data[n][m]
            }
        }

        return Matrix(newMatrix)
    }

    val determinant: Float by lazy {
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

    val isInvertible: Boolean by lazy { determinant != 0f }

    fun subMatrixOf(mToDrop: Int, nToDrop: Int): Matrix {

        var currentM = 0
        val newMatrix = Array(size - 1) { m ->
            if (m == mToDrop) {
                ++currentM
            }

            var currentN = 0
            val row = Array(size - 1) { n ->
                if (n == nToDrop) {
                    ++currentN
                }
                data[currentM][currentN++]
            }
            currentM++
            row
        }

        return Matrix(newMatrix)
    }

    fun minor(mToDrop: Int, nToDrop: Int): Float {
        val subMatrix = subMatrixOf(mToDrop, nToDrop)
        return subMatrix.determinant
    }

    fun cofactor(mToDrop: Int, nToDrop: Int): Float {
        val minor = minor(mToDrop, nToDrop)
        val isEven = (mToDrop + nToDrop) and 1 == 0
        return if (isEven) minor else -1 * minor
    }

    fun inverse(): Matrix {
        if (!isInvertible) throw Exception("Matrix is not invertible and can not be inverted")

        val M2 = Array(size) { m ->
            Array(size) { n ->
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Matrix

        if (!data.contentDeepEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentDeepHashCode()
    }

    companion object {
        val IDENTITY = Matrix4 {
            r1(1, 0, 0, 0)
            r2(0, 1, 0, 0)
            r3(0, 0, 1, 0)
            r4(0, 0, 0, 1)
        }

        fun translation(x: Number, y: Number, z: Number) = Matrix4 {
            r1(1, 0, 0, x)
            r2(0, 1, 0, y)
            r3(0, 0, 1, z)
            r4(0, 0, 0, 1)
        }

        fun scaling(x: Number, y: Number, z: Number) = Matrix4 {
            r1(x, 0, 0, 0)
            r2(0, y, 0, 0)
            r3(0, 0, z, 0)
            r4(0, 0, 0, 1)
        }

        fun rotationX(radians: Number): Matrix {
            val r = radians.toFloat()
            return Matrix4 {
                r1(1, 0, 0, 0)
                r2(0, cos(r), -sin(r), 0)
                r3(0, sin(r), cos(r), 0)
                r4(0, 0, 0, 1)
            }
        }

        fun rotationY(radians: Number): Matrix {
            val r = radians.toFloat()
            return Matrix4 {
                r1(cos(r), 0, sin(r), 0)
                r2(0, 1, 0, 0)
                r3(-sin(r), 0, cos(r), 0)
                r4(0, 0, 0, 1)
            }
        }

        fun rotationZ(radians: Number): Matrix {
            val r = radians.toFloat()
            return Matrix4 {
                r1(cos(r), -sin(r), 0, 0)
                r2(sin(r), cos(r), 0, 0)
                r3(0, 0, 1, 0)
                r4(0, 0, 0, 1)
            }
        }

        fun shearing(
            xToY: Number,
            xtoZ: Number,
            yToX: Number,
            yToZ: Number,
            zToX: Number,
            zToY: Number
        ): Matrix = Matrix4 {
            r1(1, xToY, xtoZ, 0)
            r2(yToX, 1, yToZ, 0)
            r3(zToX, zToY, 1, 0)
            r4(0, 0, 0, 1)
        }
    }
}

class MatrixBuilder4 {
    private lateinit var row1: Array<Float>
    private lateinit var row2: Array<Float>
    private lateinit var row3: Array<Float>
    private lateinit var row4: Array<Float>

    fun r1(a11: Number, a12: Number, a13: Number, a14: Number) {
        row1 = arrayOf(a11.toFloat(), a12.toFloat(), a13.toFloat(), a14.toFloat())
    }

    fun r2(a21: Number, a22: Number, a23: Number, a24: Number) {
        row2 = arrayOf(a21.toFloat(), a22.toFloat(), a23.toFloat(), a24.toFloat())
    }

    fun r3(a31: Number, a32: Number, a33: Number, a34: Number) {
        row3 = arrayOf(a31.toFloat(), a32.toFloat(), a33.toFloat(), a34.toFloat())
    }

    fun r4(a41: Number, a42: Number, a43: Number, a44: Number) {
        row4 = arrayOf(a41.toFloat(), a42.toFloat(), a43.toFloat(), a44.toFloat())
    }

    fun toMatrix(): Matrix {
        return Matrix(arrayOf(row1, row2, row3, row4))
    }
}

@Suppress("FunctionName")
fun Matrix4(create: MatrixBuilder4.() -> Unit): Matrix {
    val builder = MatrixBuilder4()
    builder.create()
    return builder.toMatrix()
}

class MatrixBuilder3 {
    private lateinit var row1: Array<Float>
    private lateinit var row2: Array<Float>
    private lateinit var row3: Array<Float>

    fun r1(a11: Number, a12: Number, a13: Number) {
        row1 = arrayOf(a11.toFloat(), a12.toFloat(), a13.toFloat())
    }

    fun r2(a21: Number, a22: Number, a23: Number) {
        row2 = arrayOf(a21.toFloat(), a22.toFloat(), a23.toFloat())
    }

    fun r3(a31: Number, a32: Number, a33: Number) {
        row3 = arrayOf(a31.toFloat(), a32.toFloat(), a33.toFloat())
    }

    fun toMatrix(): Matrix {
        return Matrix(arrayOf(row1, row2, row3))
    }
}

@Suppress("FunctionName")
fun Matrix2(create: MatrixBuilder2.() -> Unit): Matrix {
    val builder = MatrixBuilder2()
    builder.create()
    return builder.toMatrix()
}

class MatrixBuilder2 {
    private lateinit var row1: Array<Float>
    private lateinit var row2: Array<Float>

    fun r1(a11: Number, a12: Number) {
        row1 = arrayOf(a11.toFloat(), a12.toFloat())
    }

    fun r2(a21: Number, a22: Number) {
        row2 = arrayOf(a21.toFloat(), a22.toFloat())
    }

    fun toMatrix(): Matrix {
        return Matrix(arrayOf(row1, row2))
    }
}

@Suppress("FunctionName")
fun Matrix3(create: MatrixBuilder3.() -> Unit): Matrix {
    val builder = MatrixBuilder3()
    builder.create()
    return builder.toMatrix()
}
