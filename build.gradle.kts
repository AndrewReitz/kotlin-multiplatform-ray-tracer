plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false version versions.kotlin
    kotlin("js") apply false

    id("com.github.ben-manes.versions") version "0.33.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven("https://jitpack.io")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates").configure {
    fun String.isNonStable(): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { this.toUpperCase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(this)
        return isStable.not()
    }

    rejectVersionIf {
        candidate.version.isNonStable()
    }
}
