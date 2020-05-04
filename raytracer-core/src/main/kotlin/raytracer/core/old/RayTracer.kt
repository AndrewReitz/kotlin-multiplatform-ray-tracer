package raytracer.core.old

import java.awt.image.BufferedImage

class RayTracer(private val scene: Scene) {

    fun draw(): BufferedImage {
//        val height = scene.height
//        val width = scene.width
//
//        val imageData = BufferedImage(width, height, TYPE_3BYTE_BGR)
//
//        // single camera mode
//        val camera = scene.camera
//
//        val imagePlaneWidth = camera.imagePlaneWidth
//        val imagePlaneHeight = (imagePlaneWidth * height.toDouble()) / width.toDouble()
//
//        val top = imagePlaneHeight / 2
//        val bottom = -1 * imagePlaneHeight / 2
//        val left = -1 * imagePlaneWidth / 2
//        val right = imagePlaneWidth / 2
//
//        for (row in 0 until height) {
//            for (col in 0 until width) {
//
//                // wtf is idx?
//                val idx = (row * width * 3) + col * 3
//
//                // coefficient of what!?
//                var coefficient = 1.0
//                var level = 1
//
//                var pixelColor = Color(0x0, 0x0, 0x0)
//
//                val u = left + (right - left) * (col + 0.5) / width
//                val v = bottom + (top - bottom) * (row + 0.5) / height
//
//                val viewRay = camera.getRay(u = u, v = v)
//
////                do {
//                    var t = T(Double.MAX_VALUE)
//                    val shape = scene.shapes.firstOrNull {
//                        val hit = it.isHit(viewRay, t)
//                        t = hit.t
//                        hit.isHit
//                    } ?: break
//
//                    val color: Color = when (shape.shader) {
//                        is BlinnPhong -> {
//                            scene.lights.map { light ->  // we have reflections here
//                                shape.calculateColor(viewRay, light, t, scene.shapes)
//                            }.reduce { acc, i -> acc + i }
//                        }
//                        is Lambertion -> { // coef = 0 here
//                            coefficient = 0.0
//                            scene.lights.map {
//                                shape.calculateColor(viewRay, it, t, scene.shapes)
//                            }.reduce { accumulated, color ->
//                                accumulated + color
//                            }
//                        }
//                    }
//
//                    pixelColor += color
////                } while (level < 10 && coefficient > 0)
//
//                imageData[row, col] = pixelColor.toColorInt()
//            }
//        }
//
//        return imageData
        TODO()
    }

    private operator fun BufferedImage.set(x: Int, y: Int, rgb: Int) = setRGB(x, y, rgb)
}