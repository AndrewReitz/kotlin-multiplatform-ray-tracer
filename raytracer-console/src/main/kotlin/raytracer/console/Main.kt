package raytracer.console

import raytracer.core.Scene
import java.io.File

fun main(args: Array<String>) {
    Scene(File("output.png"), 400, 400).render()
}
