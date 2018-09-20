package raytracer.console

import kotlinx.coroutines.runBlocking
import raytracer.core.Scene
import java.io.File

fun main(args: Array<String>) = runBlocking {
    Scene(File("output.png"), 400, 400).render()
}
