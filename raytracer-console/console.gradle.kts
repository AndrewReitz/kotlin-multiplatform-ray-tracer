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
      }
    }

    val nativeMain = create("nativeMain") {
      dependsOn(commonMain)
    }

    val linuxX64Main by getting {
      dependsOn(nativeMain)
    }

    val macosX64Main by getting {
      dependsOn(nativeMain)
    }
  }
}
