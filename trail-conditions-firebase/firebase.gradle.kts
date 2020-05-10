plugins {
  kotlin("js")
  id("gcp")
}

dependencies {
  implementation(project(":trail-conditions-networking"))

  implementation(kotlin("stdlib-js"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${versions.coroutines}")
}

kotlin {
  target {
    nodejs()
    useCommonJs()
  }
}

gcp {
  targets += trails.gradle.GcpTarget(
    name = "morcTrails",
    trigger = "http",
    runtime = "nodejs10",
    flags = listOf("--allow-unauthenticated", "--project", "mn-trail-functions")
  )
}
