package raytracer.core

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_3BYTE_BGR
import java.io.File
import javax.imageio.ImageIO

class Scene(
    private val output: File,
    private val height: Int,
    private val width: Int
) {
    fun render() {
        val image = BufferedImage(width, height, TYPE_3BYTE_BGR)

        for (column in 0 until width) {
            for (row in 0 until height) {
                image.setRGB(column, row, 0xff00ff)
            }
        }

        ImageIO.write(image, "png", output)
    }
}