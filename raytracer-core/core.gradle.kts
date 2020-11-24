plugins {
  id("multiplatform-common")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":raytracer-math"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(project(":raytracer-math-test"))
      }
    }

    val nativeMain = create("nativeMain") {
      dependsOn(commonMain)
    }

    val mingwX64Main by getting {
      dependsOn(nativeMain)
    }

    val linuxX64Main by getting {
      dependsOn(nativeMain)
    }

    val macosX64Main by getting {
      dependsOn(nativeMain)
    }
  }
}
