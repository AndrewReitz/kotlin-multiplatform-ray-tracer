plugins {
  id("multiplatform-common")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(project(":raytracer-math"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(project(":raytracer-math-test"))
      }
    }
  }
}
