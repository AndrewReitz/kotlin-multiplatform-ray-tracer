package raytracer.core

import raytracer.math.Float3

data class Color(
    val red: Float,
    val green: Float,
    val blue: Float
) : Float3 {
    override val x = red
    override val y = green
    override val z = blue

    operator fun plus(color: Color) = Color(
        red = red + color.red,
        blue = blue + color.blue,
        green = green + color.green
    )

    operator fun minus(color: Color) = Color(
        red = red - color.red,
        blue = blue - color.blue,
        green = green - color.green
    )

    operator fun times(scalar: Number): Color {
        val value = scalar.toFloat()
        return Color(
            red = red * value,
            blue = blue * value,
            green = green * value
        )
    }

    operator fun times(color: Color) = Color(
        red = red * color.red,
        blue = blue * color.blue,
        green = green * color.green
    )

    companion object {
        val Black: Color = Color(0f, 0f, 0f)
        val White: Color = Color(1f, 1f, 1f)
        val Red: Color = Color(1f, 0f, 0f)
        val Blue: Color = Color(0f, 0f, 1f)
        val Green: Color = Color(0f, 1f, 0f)
    }
}

fun Color(
    red: Number = 0,
    green: Number = 0,
    blue: Number = 0
) = Color(
    red = red.toFloat(),
    green = green.toFloat(),
    blue = blue.toFloat()
)
