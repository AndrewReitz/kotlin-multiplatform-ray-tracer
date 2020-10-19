plugins {
  kotlin("jvm")
  kotlin("kapt")
  kotlin("plugin.serialization")

  id("com.github.johnrengelman.shadow") version "6.1.0"

  id("twitter-api-keys")
  id("firebase-credentials")
  id("kotlin-config-writer")
}

configurations.create("invoker")

dependencies {
  implementation(project(":trail-conditions-networking"))

  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions.coroutines}")

  implementation("com.google.firebase:firebase-admin:7.0.1")
  implementation("org.twitter4j:twitter4j-core:4.0.7")

  kapt("com.google.dagger:dagger-compiler:2.29.1")
  implementation("com.google.dagger:dagger:2.29.1")

  // Every function needs this dependency to get the Functions Framework API.
  compileOnly("com.google.cloud.functions:functions-framework-api:1.0.1")

  // To run function locally using Functions Framework's local invoker
  "invoker"("com.google.cloud.functions.invoker:java-function-invoker:1.0.0-alpha-2-rc5")

  testImplementation(kotlin("test-annotations-common", versions.kotlin))
  testImplementation(kotlin("test-junit", versions.kotlin))
  testImplementation("com.google.cloud.functions:functions-framework-api:1.0.1")
  testImplementation("io.ktor:ktor-client-mock-jvm:${versions.ktor}")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.coroutines}")
  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

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

apply(from = "runFunction.gradle")

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
  destinationDirectory.set(file("$buildDir/gcp"))
  mergeServiceFiles()
}

val deployAggregatorStaging = tasks.register<Exec>("deployAggregatorStaging") {
  dependsOn(tasks.named("shadowJar"))

  workingDir = project.buildDir
  commandLine = listOf(
    "gcloud",
    "functions",
    "deploy",
    "trail-aggregator-staging",
    "--entry-point",
    "trail.gcp.TrailAggregatorFunction",
    "--runtime",
    "java11",
    "--trigger-http",
    "--memory",
    "256MB",
    "--allow-unauthenticated",
    "--project",
    "mn-trail-functions",
    "--source=gcp"
  )
}

val deployNotificationStaging = tasks.register<Exec>("deployNotificationStaging") {
  dependsOn(tasks.named("shadowJar"))

  workingDir = project.buildDir
  commandLine = listOf(
    "gcloud",
    "functions",
    "deploy",
    "trail-notification-staging",
    "--entry-point",
    "trail.gcp.TrailNotificationsFunction",
    "--runtime",
    "java11",
    "--trigger-topic",
    "trailNotifications",
    "--memory",
    "256MB",
    "--project",
    "mn-trail-functions",
    "--source=gcp"
  )
}

tasks.register("deployStaging") {
  description = "publish gcp functions to staging"
  group = "deploy"
  dependsOn(deployAggregatorStaging, deployNotificationStaging)
}

val deployAggregatorRelease = tasks.register<Exec>("deployAggregatorRelease") {
  dependsOn(tasks.named("shadowJar"))

  workingDir = project.buildDir
  commandLine = listOf(
    "gcloud",
    "functions",
    "deploy",
    "trail-aggregator",
    "--entry-point",
    "trail.gcp.TrailAggregatorFunction",
    "--runtime",
    "java11",
    "--trigger-http",
    "--memory",
    "256MB",
    "--allow-unauthenticated",
    "--project",
    "mn-trail-functions",
    "--source=gcp"
  )
}

val deployNotificationRelease = tasks.register<Exec>("deployNotificationRelease") {
  dependsOn(tasks.named("shadowJar"))

  workingDir = project.buildDir
  commandLine = listOf(
    "gcloud",
    "functions",
    "deploy",
    "trail-notification",
    "--entry-point",
    "trail.gcp.TrailNotificationsFunction",
    "--runtime",
    "java11",
    "--trigger-topic",
    "trailNotifications",
    "--memory",
    "256MB",
    "--project",
    "mn-trail-functions",
    "--source=gcp"
  )
}

tasks.register("deployRelease") {
  description = "publish gcp functions to staging"
  group = "deploy"
  dependsOn(deployAggregatorRelease, deployNotificationRelease)
}

