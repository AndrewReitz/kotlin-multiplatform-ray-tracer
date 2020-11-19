plugins {
  id("multiplatform-common")
}

kotlin {
  linuxX64 {
    binaries {
      executable {
        entryPoint = "raytracer.console.main"
      }
    }
  }
  macosX64 {
    binaries {
      executable {
        entryPoint = "raytracer.console.main"
      }
    }
  }
  js {
    nodejs()
  }
  jvm()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":raytracer-math"))
        implementation(project(":raytracer-core"))

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutines")
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
      }
    }

    val jsMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutines")
      }
    }

    val nativeMain = create("nativeMain") {
      dependsOn(commonMain)
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutines")
      }
    }

    val linuxX64Main by getting {
      dependsOn(nativeMain)
    }

    val macosX64Main by getting {
      dependsOn(nativeMain)
    }
  }
}
