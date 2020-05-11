package raytracer.console

import java.io.File

actual fun writeToFile(text: String, filePath: String) {
  File(filePath).writeText(text)
}
