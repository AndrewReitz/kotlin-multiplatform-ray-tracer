import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.60"
}

allprojects {
    group = "cash.andrew.raytracer"
    version = "1.0-SNAPSHOT"

    repositories {
        jcenter()
    }
}

subprojects {
    apply(plugin = "kotlin")

    dependencies {
        compile(kotlin("stdlib"))
    }
}
