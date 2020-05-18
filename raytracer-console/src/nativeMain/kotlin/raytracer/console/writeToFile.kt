package raytracer.console

import platform.posix.*

actual fun writeToFile(text: String, filePath: String) {
  val fp = fopen(filePath, "w+")
  fprintf(fp, text)
  fclose(fp)
}
