package raytracer.console

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.serialization.decodeFromString
import raytracer.core.Scene
import raytracer.core.runBlocking
import raytracer.serialization.JSON
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@ExperimentalTime
class RayTracer : CliktCommand() {
    private val source: String by argument()
    private val dest: String by argument()

    override fun run() = runBlocking {
        val yaml = readFromFile(source)
        val scene: Scene = JSON.decodeFromString(yaml)

        val (canvas, renderTime) = measureTimedValue {
            scene.render()
        }

        echo("Rendering took ${renderTime.inSeconds} seconds")

        val imageToDiskTime = measureTime {
            writeToFile(canvas.toPpm(), dest)
        }

        echo("Writing image to disk took ${imageToDiskTime.inSeconds} seconds")
    }
}

@ExperimentalTime
fun main(args: Array<String>) = RayTracer().main(args)
