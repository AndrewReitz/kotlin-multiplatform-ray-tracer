plugins {
  kotlin("jvm")
  kotlin("kapt")
  kotlin("plugin.serialization")
}

dependencies {
  implementation(kotlin("stdlib", versions.kotlin))

  api("io.ktor:ktor-client-core:${versions.ktor}")
  api("io.ktor:ktor-client-json:${versions.ktor}")
  api("io.ktor:ktor-client-serialization:${versions.ktor}")
  api("io.ktor:ktor-client-okhttp:${versions.ktor}")

  api("org.jetbrains.kotlinx:kotlinx-serialization-core:${versions.serialization}")

  api("com.github.AndrewReitz.andrew-kotlin-commons:andrew-kotlin-commons:master-SNAPSHOT")

  testImplementation(kotlin("test-annotations-common", versions.kotlin))
  testImplementation(kotlin("test-junit", versions.kotlin))
  testImplementation("io.ktor:ktor-client-mock-jvm:${versions.ktor}")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.coroutines}")
  testImplementation("io.ktor:ktor-client-mock:${versions.ktor}")
}
