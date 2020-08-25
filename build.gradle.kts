plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false version versions.kotlin
    kotlin("js") apply false

    id("com.github.ben-manes.versions") version "0.29.0"
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
