@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("multiplatform")
}

kotlin {
  jvm()
  js { nodejs() }
  linuxX64()
  macosX64()
  mingwX64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common"))
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))
      }
    }
    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(kotlin("stdlib-js"))
      }
    }
    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
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

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += listOf("-Xno-param-assertions", "-Xno-call-assertions")
}
