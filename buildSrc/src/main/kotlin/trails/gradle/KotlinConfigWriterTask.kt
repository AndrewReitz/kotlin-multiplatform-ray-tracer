package trails.gradle

import com.squareup.kotlinpoet.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File
import java.util.*

@CacheableTask
abstract class KotlinConfigWriterTask : DefaultTask() {

  @get:Input
  abstract var keyValuePairs: Map<String, String>

  @get:Input
  abstract var className: String

  @get:OutputDirectory
  abstract var kotlinConfigFile: File

  @TaskAction
  fun generate() {
    val file = FileSpec.builder("trails.config", className)
      .addType(
        TypeSpec.objectBuilder("Keys")
          .apply {
            keyValuePairs.forEach { (key, value) ->
              addProperty(
                PropertySpec.builder(key.toUpperCase(Locale.ROOT), String::class, KModifier.CONST).initializer("%S", value) .build()
              )
            }
          }
          .build()
      ).build()

    file.writeTo(kotlinConfigFile)
  }
}
