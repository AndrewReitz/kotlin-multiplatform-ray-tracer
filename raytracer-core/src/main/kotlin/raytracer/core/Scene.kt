package raytracer.core

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_3BYTE_BGR
import java.awt.image.RenderedImage

class Scene(
    val height: Int,
    val width: Int
) {

    val lights: List<Light> = emptyList()
    val shapes: List<Shape> = emptyList()
    val camera: Camera = TODO()


    fun render(): RenderedImage  {
        val image = BufferedImage(width, height, TYPE_3BYTE_BGR)

        for (column in 0 until width) {
            for (row in 0 until height) {
                image.setRGB(column, row, 0xff00ff)
            }
        }

        return image
    }
}