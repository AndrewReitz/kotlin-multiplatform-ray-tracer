package trails.gradle

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

@CacheableTask
abstract class GcpSetupTask : DefaultTask() {

  @get:Input
  abstract var gcpTarget: String

  @get:OutputDirectory
  abstract val outputDir: DirectoryProperty

  @TaskAction
  fun setup() {
    outputDir.file("index.js").get().asFile.writeText(
      "exports.$gcpTarget = require(\"${project.rootProject.name}-${project.name}\").$gcpTarget\n"
    )

    outputDir.file(".gcloudignore").get().asFile.writeText("")
  }
}
