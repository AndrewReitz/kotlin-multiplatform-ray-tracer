import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.70"
    id("com.gradle.build-scan") version "1.15.2"
}

buildScan {
    setTermsOfServiceUrl("https://gradle.com/terms-of-service")
    setTermsOfServiceAgree("yes")
    publishAlways()
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
