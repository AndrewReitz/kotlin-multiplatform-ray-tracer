plugins {
  kotlin("jvm")
  application
}

application {
  mainClassName = "raytracer.console.jvm.MainKt"
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":raytracer-console"))
}
