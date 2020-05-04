package raytracer.core.old

inline class T(val value: Double) {
    companion object {
        private val DEFAULT = T(Double.MAX_VALUE)
        val default get() = DEFAULT
    }
}
