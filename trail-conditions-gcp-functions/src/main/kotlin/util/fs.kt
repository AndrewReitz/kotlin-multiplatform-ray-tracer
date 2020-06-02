package util

/**
 * Reading and writing files on nodejs
 */
@JsModule("fs")
@Suppress("ClassName")
external object fs {
  fun readFileSync(path: String, encoding: String): String
  fun existsSync(path: String): Boolean
  fun writeFileSync(path: String, data: String)
}
