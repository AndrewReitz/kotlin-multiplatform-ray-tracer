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
  jvm {
    this.withJavaEnabled
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":raytracer-math"))
        implementation(project(":raytracer-core"))

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.6")
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.6")
      }
    }

    val jsMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.6")
      }
    }

    val nativeMain = create("nativeMain") {
      dependsOn(commonMain)
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.6")
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
