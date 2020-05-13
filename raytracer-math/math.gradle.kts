plugins {
  id("multiplatform-common")
}

kotlin {
  sourceSets {
    val commonTest by getting {
      dependencies {
        implementation(project(":raytracer-math-test"))
      }
    }
  }
}
