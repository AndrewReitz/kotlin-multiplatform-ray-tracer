/**
 * Convert pascalCase to SCREAMING_SNAKE_CASE
 */
fun String.toScreamingSnakeCase() = map {
  when {
    it.isUpperCase() -> "_$it"
    it == '.' -> "_"
    else -> it.toUpperCase()
  }
}.joinToString("")
