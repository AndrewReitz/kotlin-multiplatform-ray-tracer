package raytracer.scene

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.TermUi.echo
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.encodeToString
import raytracer.core.Camera
import raytracer.core.Canvas
import raytracer.core.World
import raytracer.core.runBlocking
import raytracer.serialization.JSON
import java.io.File
import java.nio.file.Paths
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptAcceptedLocation
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.acceptedLocations
import kotlin.script.experimental.api.compilerOptions
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.ide
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun main(args: Array<String>) = Raytracer()
    .subcommands(GenerateImage(), GenerateSceneFile())
    .main(args)

/**
 * Simple setup for kotlin script loading.
 * Use .kts file ending to intellij supports syntax highlighting.
 */
@KotlinScript(fileExtension = "scene.kts")
abstract class RaytracerScript

lateinit var command: RaytracerCommand

sealed class RaytracerCommand(name: String? = null) : CliktCommand(name = name)

class Raytracer : CliktCommand() {
    override fun run() = Unit
}

class GenerateImage : RaytracerCommand(name = "image") {
    private val source: File by argument().file(mustExist = true)
    val dest by argument().file()

    override fun run() {
        command = this
        runScript(source)
    }
}

class GenerateSceneFile : RaytracerCommand(name = "scene") {
    private val source by argument().file(mustExist = true)
    val dest by argument().file()

    override fun run() {
        command = this
        runScript(source)
    }
}

fun runScript(source: File) {
    val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<RaytracerScript> {
        jvm {
            compilerOptions(
                "-Xopt-in=kotlin.time.ExperimentalTime",
                "-Xno-param-assertions",
                "-Xno-call-assertions"
            )
            defaultImports(
                "raytracer.core.*",
                "raytracer.core.shapes.*",
                "raytracer.core.patterns.*",
                "raytracer.math.*",
                "kotlin.math.*",
                "raytracer.scene.Scene"
            )
            dependenciesFromCurrentContext(wholeClasspath = true)
        }
        ide {
            acceptedLocations(ScriptAcceptedLocation.Everywhere)
        }
    }

    val result = BasicJvmScriptingHost().eval(source.toScriptSource(), compilationConfiguration, null)

    result.reports
        .filter { it.severity == ScriptDiagnostic.Severity.ERROR }
        .forEach { (_, message, _, sourcePath, location: SourceCode.Location?, _) ->
            val sourceFile = Paths.get(requireNotNull(sourcePath))
            echo("There was an error with script \"${sourceFile.fileName}\" at line ${location?.start?.line}: $message")
        }
}

/** Main entry point for scene files */
@Suppress("UNUSED_VARIABLE", "FunctionName", "unused")
@ExperimentalTime
fun Scene(world: World, camera: Camera) {
    val forceExhaustive: Any = when (val c = command) {
        is GenerateImage -> generateImage(world, camera, c.dest)
        is GenerateSceneFile -> writeOutSceneJson(raytracer.core.Scene(world = world, camera = camera), c.dest)
    }
}

@ExperimentalTime
fun generateImage(world: World, camera: Camera, output: File) = runBlocking {
    echo("Generating image...")
    val canvas: Canvas
    val time = measureTime {
        canvas = camera.render(world)
    }

    output.writeText(canvas.toPpm())
    echo("Generating image took ${time.inSeconds} seconds")
}

fun writeOutSceneJson(scene: raytracer.core.Scene, output: File) {
    runCatching {
        val jsonScene = JSON.encodeToString(scene)
        output.writeText(jsonScene)
        output.appendText("\n")
        echo("scene file written to ${output.absolutePath}")
    }.onFailure {
        echo("Error creating json file: ${it.message}")
    }
}
