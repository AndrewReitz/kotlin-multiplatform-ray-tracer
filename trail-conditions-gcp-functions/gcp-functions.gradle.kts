plugins {
  kotlin("js")
  id("gcp")
  id("twitter-api-keys")
  id("kotlin-config-writer")
}

dependencies {
  implementation(project(":trail-conditions-networking"))

  implementation(kotlin("stdlib-js"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${versions.coroutines}")

  implementation(npm("twitter"))
}

kotlin {
  target {
    nodejs()
    useCommonJs()
  }
}

gcp {
  targets += listOf(
    trails.gradle.GcpTarget(
      name = "morcTrails",
      trigger = "http",
      runtime = "nodejs10",
      flags = listOf("--allow-unauthenticated", "--project", "mn-trail-functions")
    ),
    trails.gradle.GcpTarget(
      name = "trailAggregator",
      trigger = "http",
      runtime = "nodejs10",
      flags = listOf("--allow-unauthenticated", "--project", "mn-trail-functions")
    )
  )
}

kotlinConfigWriter {
  val consumerKey: String by project
  val consumerSecret: String by project
  val accessTokenKey: String by project
  val accessTokenSecret: String by project

  put("consumer_key" to consumerKey)
  put("consumer_secret" to consumerSecret)
  put("access_token_key" to accessTokenKey)
  put("access_token_secret" to accessTokenSecret)
}
