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
        implementation(kotlin("stdlib-common"))

        api("io.ktor:ktor-client-core:${versions.ktor}")
        api("io.ktor:ktor-client-json:${versions.ktor}")
        api("io.ktor:ktor-client-serialization:${versions.ktor}")

        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${versions.serialization}")

        api("com.github.AndrewReitz.andrew-kotlin-commons:andrew-kotlin-commons:master-SNAPSHOT")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.5")

        implementation("io.ktor:ktor-client-mock:${versions.ktor}")
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))

        api("io.ktor:ktor-client-okhttp:${versions.ktor}")
        api("io.ktor:ktor-client-json-jvm:${versions.ktor}")
        api("io.ktor:ktor-client-serialization-jvm:${versions.ktor}")

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${versions.serialization}")
      }
    }
    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))

        implementation("io.ktor:ktor-client-mock-jvm:${versions.ktor}")
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(kotlin("stdlib-js"))

        api("io.ktor:ktor-client-js:${versions.ktor}")
        api("io.ktor:ktor-client-serialization-js:${versions.ktor}")
        implementation(npm("abort-controller", "3.0.0")) // required by ktor but not pulled in
        implementation(npm("node-fetch", "2.6.0")) // required by ktor but not pulled in
        implementation(npm("text-encoding", "0.7.0")) // required by ktor but not pulled in

        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${versions.serialization}")

      }
    }
    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))

        implementation("io.ktor:ktor-client-mock-js:${versions.ktor}")
      }
    }
  }
}
