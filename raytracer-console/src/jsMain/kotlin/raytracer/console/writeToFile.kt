package raytracer.console

actual fun writeToFile(text: String, filePath: String) {
  val fs = js("require('fs')")
  fs.writeFileSync(filePath, text)
}
