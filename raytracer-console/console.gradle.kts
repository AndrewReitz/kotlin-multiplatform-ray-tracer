plugins {
  id("multiplatform-common")
}

kotlin {
  mingwX64 {
    binaries {
      executable {
        entryPoint = "raytracer.console.main"
      }
    }
  }
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
        implementation(project(":raytracer-core"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
      }
    }
  }
}
