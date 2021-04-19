plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib", libs.versions.kotlin.get()))
  implementation(kotlin("gradle-plugin", libs.versions.kotlin.get()))
  implementation(kotlin("serialization", libs.versions.kotlin.get()))
}
