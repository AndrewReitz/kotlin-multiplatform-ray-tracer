plugins {
  id("multiplatform-common")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.kotlinx.serialization.core)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(projects.raytracerMathTest)
      }
    }
  }
}
