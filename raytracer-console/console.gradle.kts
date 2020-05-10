plugins {
  kotlin("jvm")
  application
}

application {
  mainClassName = "raytracer.console.MainKt"
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":raytracer-core"))
  implementation(project(":raytracer-math"))
}
