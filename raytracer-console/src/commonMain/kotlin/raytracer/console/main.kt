package raytracer.console

import raytracer.core.Camera
import raytracer.core.Canvas
import raytracer.core.Color
import raytracer.core.Material
import raytracer.core.PointLight
import raytracer.core.World
import raytracer.core.patterns.CheckerPattern
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

    val checkerboardMaterial = Material(
        specular = 1,
        pattern = CheckerPattern(Color.Black, Color.White),
        reflective = 0.1,
        ambient = 0.05
    )

    val floor = Plane(
        material = checkerboardMaterial,
        transform = Matrix.rotationY(PI / 6) * Matrix.translation(0, -1, 0)
    )

    val leftWall = Plane(
        material = checkerboardMaterial,
        transform = Matrix.translation(0, 0, 15) * Matrix.rotationY(PI / 6) * Matrix.rotationX(PI / 2)
    )

    val rightWall = Plane(
        material = checkerboardMaterial,
        transform = Matrix.translation(0, 0, 15) * Matrix.rotationY(5 * PI / 6) * Matrix.rotationX(PI / 2)
    )

    val leftSphere = Sphere(
        material = Material(color = Color.Blue, shininess = 30, specular = 0.5, ambient = 0),
        transform = Matrix.translation(-3, 0, 8)
    )

    val middleSphere = Sphere(
        material = Material(color = Color.Red, shininess = 3, specular = 0.1),
        transform = Matrix.translation(-0.7, 0.5, 8) * Matrix.scaling(1.5, 1.5, 1.5)
    )

    val rightSphere = Sphere(
        material = Material(color = Color.Green),
        transform = Matrix.translation(1.2, 0, 8) * Matrix.scaling(0.8, 0.8, 0.8)
    )

    val glassSphere = Sphere(
        material = Material(
            color = Color.Black,
            refractiveIndex = 1.55,
            transparency = 1,
            reflective = 0.5,
            diffuse = 0.1,
            ambient = 0,
            specular = 1,
            shininess = 300
        ),
        transform = Matrix.translation(0, 1, 0) * Matrix.scaling(2.5, 2.5, 2.5)
    )

    val world = World(
        lights = listOf(
            PointLight(
                Point(-8, 20, -10),
                Color.White
            )
        ),
        objects = listOf(
            floor,
            leftWall,
            rightWall,
            leftSphere,
            middleSphere,
            rightSphere,
            glassSphere
        )
    )

    val scale = 4
    val camera = Camera(
        hsize = 500 * scale,
        vsize = 250 * scale,
        fieldOfView = PI / 3,
        transform = viewTransform(
            from = Point(-1, 1, -10),
            to = Point(0, 0.5, 4),
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
