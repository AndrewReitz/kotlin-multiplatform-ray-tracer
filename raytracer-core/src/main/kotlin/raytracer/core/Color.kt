package raytracer.core

data class Color(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0
) {
    operator fun plus(color: Color) = Color(
            red = red + color.red,
            blue = blue + color.blue,
            green = green + color.green
    )

    fun toColorInt() = (red shl 16) or (green shl 8) or blue
}