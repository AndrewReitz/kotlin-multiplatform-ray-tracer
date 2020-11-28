package raytracer.console

import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fprintf

actual fun writeToFile(text: String, filePath: String) {
    val fp = fopen(filePath, "w+")
    fprintf(fp, text)
    fclose(fp)
}
