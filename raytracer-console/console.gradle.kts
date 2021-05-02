plugins {
  application
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

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.raytracerCore)
        implementation(projects.raytracerSerialization)
        implementation(libs.kotlinx.coroutines)
        api(libs.clikt)
      }
    }
  }
}

application {
  mainClass.set("raytracer.console.MainKt")
}
