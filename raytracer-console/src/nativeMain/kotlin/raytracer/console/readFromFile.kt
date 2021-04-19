package raytracer.console

import platform.posix.EOF
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.getc

actual fun readFromFile(filePath: String): String {
    val fp = fopen(filePath, "r")

    val text = arrayListOf<Char>()
    var c = getc(fp)
    while (c != EOF) {
        text.add(c.toChar())
        c = getc(fp)
    }

    fclose(fp)

    return text.joinToString(separator = "")
}
