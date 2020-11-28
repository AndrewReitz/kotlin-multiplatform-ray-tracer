package raytracer.console

import raytracer.core.Camera
import raytracer.core.Canvas
import raytracer.core.Color
import raytracer.core.Material
import raytracer.core.PointLight
import raytracer.core.World
import raytracer.core.runBlocking
import raytracer.core.shapes.Plane
import raytracer.core.shapes.Sphere
import raytracer.core.viewTransform
import raytracer.math.Matrix
import raytracer.math.Point
import raytracer.math.Vector
import kotlin.math.PI
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main(): Unit = runBlocking {
    println("starting...")

    val floor = Plane(
        material = Material(
            color = Color(1, 0.9, 0.9),
            specular = 0F
        ),
        transform = Matrix.scaling(10, 0.01, 10)
    )

    val middle = Sphere(
        transform = Matrix.translation(-0.5, 1, 0.5),
        material = Material(
            color = Color(0.1, 1, 0.5),
            diffuse = 0.7F,
            specular = 0.3F
        )
    )

    val right = Sphere(
        transform = Matrix.translation(1.5, 0.5, -0.5) * Matrix.scaling(0.5, 0.5, 0.5),
        material = Material(
            color = Color(0.5, 1, 0.1),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val left = Sphere(
        transform = Matrix.translation(-1.5, 0.33, -0.75) * Matrix.scaling(0.33, 0.33, 0.33),
        material = Material(
            color = Color(1, 0.8, 0.1),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val world = World(
        lights = listOf(
            PointLight(
                Point(-10, 10, -10),
                Color(1, 1, 1)
            )
        ),
        objects = listOf(
            left, right, middle, floor
        )
    )

    val camera = Camera(
        hsize = 100 * 10,
        vsize = 50 * 10,
        fieldOfView = PI / 3,
        transform = viewTransform(
            from = Point(0, 1.5, -5),
            to = Point(0, 1, 0),
            up = Vector(0, 1, 0)
        )
    )

    val canvas: Canvas

    val time = measureTime {
        canvas = camera.render(world)
    }

    println("Tracing rays took ${time.inSeconds} seconds")

    val ppm = measureTime {
        writeToFile(canvas.toPpm(), "image.ppm")
    }

    println("PPM Took ${ppm.inSeconds} seconds")
}
