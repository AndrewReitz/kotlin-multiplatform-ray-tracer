package raytracer.console

actual fun readFromFile(filePath: String): String {
    val fs = js("require('fs')")
    return fs.writeFileSync(filePath) as String
}
