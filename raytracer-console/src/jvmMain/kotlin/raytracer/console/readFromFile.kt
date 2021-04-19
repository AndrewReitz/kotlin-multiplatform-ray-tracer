package raytracer.console

import java.io.File

actual fun readFromFile(filePath: String): String = File(filePath).readText()
