plugins {
  id("multiplatform-common")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.raytracerMath)
        implementation(kotlin("test-common"))
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
  }
}
