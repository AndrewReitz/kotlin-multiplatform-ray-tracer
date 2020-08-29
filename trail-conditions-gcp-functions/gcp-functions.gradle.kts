plugins {
  kotlin("js")
  kotlin("plugin.serialization")

  id("gcp")
  id("twitter-api-keys")
  id("firebase-credentials")
  id("kotlin-config-writer")
}

dependencies {
  implementation(project(":trail-conditions-networking"))

  implementation(kotlin("stdlib-js"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${versions.coroutines}")

  implementation(npm("twitter", "1.7.1"))
  implementation(npm("firebase-admin", "9.1.1"))
}

kotlin {
  js {
    nodejs()
    useCommonJs()
  }
}

gcp {
  targets += listOf(
    trails.gradle.GcpTarget(
      name = "trailAggregator",
      trigger = "http",
      runtime = "nodejs12",
      flags = listOf("--allow-unauthenticated", "--project", "mn-trail-functions")
    ),
    trails.gradle.GcpTarget(
      name = "trailNotifications",
      trigger = "topic",
      runtime = "nodejs12",
      flags = listOf("trailNotifications", "--project", "mn-trail-functions")
    )
  )
}

kotlinConfigWriter {
  val consumerKey: String by project
  val consumerSecret: String by project
  val accessTokenKey: String by project
  val accessTokenSecret: String by project

  val firebaseProjectId: String by project
  val firebaseClientEmail: String by project
  val firebasePrivateKey: String by project

  put("consumer_key" to consumerKey)
  put("consumer_secret" to consumerSecret)
  put("access_token_key" to accessTokenKey)
  put("access_token_secret" to accessTokenSecret)
  put("firebase_project_id" to firebaseProjectId)
  put("firebase_client_email" to firebaseClientEmail)
  put("firebase_private_key" to firebasePrivateKey)
}
