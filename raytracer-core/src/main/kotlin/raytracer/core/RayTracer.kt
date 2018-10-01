package raytracer.core

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_3BYTE_BGR

class RayTracer(private val scene: Scene) {

    private inline val t get() = T(Double.MAX_VALUE)

    fun draw() {
        val height = scene.height
        val width = scene.width


        val imageData = BufferedImage(width, height, TYPE_3BYTE_BGR)

        // single camera mode
        val camera = scene.camera

        val imagePlaneWidth = camera.imagePlaneWidth
        val imagePlaneHeight = (imagePlaneWidth * height.toDouble()) / width.toDouble()

        val top = imagePlaneHeight / 2
        val bottom = -1 * imagePlaneHeight / 2
        val left = -1 * imagePlaneWidth / 2
        val right = imagePlaneWidth / 2

        for (row in 0 until height) {
            for (col in 0 until width) {

                // wtf is idx?
                val idx = (row * width * 3) + col * 3

                // coefficient of what!?
                var coefficient = 1.0
                var level = 1

                var pixelColor = Color()

                val u = left + (right - left) * (col + 0.5) / width
                val v = bottom + (top - bottom) * (row + 0.5) / height

                val viewRay = camera.getRay(u = u, v = v)

                do {
                    val shape = scene.shapes.firstOrNull { it.isHit(viewRay, t) } ?: break

                    val color: Color = when(shape.shader) {
                        is BlinnPhong -> {
                            TODO()
                        }
                        is Lambertion -> {
                            // no reflections
                            coefficient = 0.0 // couldn't we break here instead?

                            scene.lights.map {
                                shape.shader.calculateColor(viewRay, it, t, scene.shapes)
                            }.reduce { accumulated, color ->
                                accumulated + color
                            }
                        }
                    }

                    pixelColor += color

                } while(level < 10 && coefficient > 0)

                val a = mutableListOf(1, 2, 3)
                a[2] = 3

                imageData[1, 2] = pixelColor
            }
        }
    }

    private operator fun BufferedImage.set(x: Int, y: Int, rgb: Int) = setRGB(x, y, rgb)
}