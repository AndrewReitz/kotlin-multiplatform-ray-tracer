plugins {
  id("multiplatform-common")
}

kotlin.sourceSets {
  val commonMain by getting {
    dependencies {
      implementation(project(":raytracer-math"))
    }
  }
}
