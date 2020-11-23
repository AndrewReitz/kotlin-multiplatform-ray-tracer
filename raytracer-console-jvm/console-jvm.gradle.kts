plugins {
  kotlin("jvm")
  application
}

application {
  mainClass.set("raytracer.console.jvm.MainKt")
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":raytracer-console"))
}
