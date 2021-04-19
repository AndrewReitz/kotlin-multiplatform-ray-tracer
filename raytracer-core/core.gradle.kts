plugins {
  id("multiplatform-common")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.raytracerMath)
        implementation(libs.kotlinx.coroutines)
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
