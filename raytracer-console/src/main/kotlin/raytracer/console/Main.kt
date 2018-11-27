package raytracer.console

import raytracer.core.Lambertion
import raytracer.core.Light
import raytracer.core.PerspecticveCamera
import raytracer.core.RayTracer
import raytracer.core.Scene
import raytracer.core.Sphere
import raytracer.math.Vector3
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val scene = Scene(
            height = 400,
            width = 400,
            lights = listOf(
                    Light(
                            position = Vector3(0, 0, 0),
                            intensity = Vector3(100, 100, 100)
                    )
            ),
            camera = PerspecticveCamera(
                    viewDirection = Vector3(1, 1, 1),
                    position = Vector3(0, 0, 0),
                    focalLength = 100.0,
                    imagePlaneWidth = 100.0,
                    height = 50.0,
                    width = 50.0
            ),
            shapes = listOf(
                    Sphere(
                            radius = 1.0,
                            position = Vector3(3, 3, 3),
                            shader = Lambertion(listOf(0xff.toDouble(), 0.0, 0.0)))
            )
    )

    val image = RayTracer(scene).draw()
    ImageIO.write(image, "png", File("output.png"))
}
