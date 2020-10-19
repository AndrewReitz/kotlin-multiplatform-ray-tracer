import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import trails.gradle.KotlinConfigWriterExtension
import trails.gradle.KotlinConfigWriterTask

plugins {
  kotlin("jvm")
}

val extension = extensions.create<KotlinConfigWriterExtension>("kotlinConfigWriter")

val outputDirectory = file("$buildDir/generated-configs")

kotlin {
  sourceSets["main"].kotlin.srcDir(outputDirectory)
}

val generateConfigTask = tasks.create<KotlinConfigWriterTask>("generateKotlinConfigFile") {
  className = extension.className
  keyValuePairs = extension.keys
  kotlinConfigFile = outputDirectory.also {
    require(it.mkdirs() || it.exists()) { "could not create config output directory"}
  }
}

afterEvaluate {
  tasks.named<KotlinJvmCompile>("compileKotlin").configure {
    dependsOn(generateConfigTask)
  }
}

plugins.findPlugin(IdeaPlugin::class)?.apply {
  model.module.sourceDirs.add(outputDirectory)
}
