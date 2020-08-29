plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  jvm()
  js { nodejs() }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common", versions.kotlin))

        api("io.ktor:ktor-client-core:${versions.ktor}")
        api("io.ktor:ktor-client-json:${versions.ktor}")
        api("io.ktor:ktor-client-serialization:${versions.ktor}")

        api("org.jetbrains.kotlinx:kotlinx-serialization-core:${versions.serialization}")

        api("com.github.AndrewReitz.andrew-kotlin-commons:andrew-kotlin-commons:master-SNAPSHOT")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common", versions.kotlin))
        implementation(kotlin("test-annotations-common", versions.kotlin))

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions.coroutines}")

        implementation("io.ktor:ktor-client-mock:${versions.ktor}")
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib", versions.kotlin))

        api("io.ktor:ktor-client-okhttp:${versions.ktor}")
        api("io.ktor:ktor-client-json-jvm:${versions.ktor}")
        api("io.ktor:ktor-client-serialization-jvm:${versions.ktor}")
      }
    }
    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit", versions.kotlin))

        implementation("io.ktor:ktor-client-mock-jvm:${versions.ktor}")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.coroutines}")
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(kotlin("stdlib-js", versions.kotlin))

        api("io.ktor:ktor-client-js:${versions.ktor}")
        api("io.ktor:ktor-client-serialization-js:${versions.ktor}")
      }
    }
    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js", versions.kotlin))

        implementation("io.ktor:ktor-client-mock-js:${versions.ktor}")
      }
    }
  }
}
