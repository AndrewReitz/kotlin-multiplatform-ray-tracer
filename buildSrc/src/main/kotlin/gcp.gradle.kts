import trails.gradle.GcpExtension
import trails.gradle.GcpSetupTask

plugins {
  kotlin("js")
}

dependencies {
  implementation(npm("@google-cloud/functions-framework", "1.5.1"))
}

val extension = extensions.create<GcpExtension>("gcp")

afterEvaluate {
  extension.targets.forEach { target ->
    val targetTaskName = target.name.capitalize()

    val setupTask = tasks.register<GcpSetupTask>("setup$targetTaskName") {
      dependsOn("build")
      gcpTarget = target.name
      outputDir.set(file("${rootProject.buildDir}/js"))
    }

    tasks.register<Exec>("run$targetTaskName") {
      group = "Run"
      description = "Runs $target firebase function for local testing"
      dependsOn(setupTask)
      workingDir("${rootProject.buildDir}/js")
      commandLine("node_modules/.bin/functions-framework", "--target=${target.name}")
    }

    tasks.register<Exec>("deploy$targetTaskName") {
      group = "Deploy"
      description = "Deploys $target to GCP Functions"
      dependsOn(setupTask)
      workingDir("${rootProject.buildDir}/js")
      commandLine(target.toArgumentList())
    }
  }
}
