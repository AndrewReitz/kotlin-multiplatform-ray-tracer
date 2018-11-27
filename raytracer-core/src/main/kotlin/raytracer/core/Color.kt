package raytracer.core

data class Color(
    val red: Double = 0.0,
    val green: Double = 0.0,
    val blue: Double = 0.0
) {

    constructor(
            red: Int = 0,
            green: Int = 0,
            blue: Int = 0
    ) : this(
            red = red.toDouble(),
            green = green.toDouble(),
            blue = blue.toDouble()
    )

    operator fun plus(color: Color) = Color(
            red = red + color.red,
            blue = blue + color.blue,
            green = green + color.green
    )

    fun toColorInt() = (red.toInt() shl 16) or (green.toInt() shl 8) or blue.toInt()
}