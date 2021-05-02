import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

description = "Wrapper to get around there being no way to apply the application plugin to multiplatform jvm project" +
        "and have it setup properly."

plugins {
  kotlin("jvm")
  application
}

application {
  mainClass.set("raytracer.console.MainKt")
}

dependencies {
  implementation(projects.raytracerConsole)
}

distributions {
  main {
    contents.duplicatesStrategy = DuplicatesStrategy.INCLUDE
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xno-param-assertions", "-Xno-call-assertions")
    jvmTarget = "11"
  }
}