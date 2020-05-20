package trails.gradle

open class KotlinConfigWriterExtension {
  internal val keys = mutableMapOf<String, String>()

  var className = "Keys"

  fun put(key: String, value: String) {
    keys[key] = value
  }

  fun put(pair: Pair<String, String>) {
    keys[pair.first] = pair.second
  }
}
