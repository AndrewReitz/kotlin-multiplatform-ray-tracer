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

    configurations {
        create("ktlint")
    }

    dependencies {
        compile(kotlin("stdlib"))
        add("ktlint", "com.github.shyiko:ktlint:0.28.0")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.create<JavaExec>("ktlint") {
        group = "formatting"
        description = "Check Kotlin code style."
        classpath = configurations["ktlint"]
        main = "com.github.shyiko.ktlint.Main"
        args = listOf("src/**/*.kt")
    }.also { ktlint -> tasks["check"].dependsOn(ktlint) }

    tasks.create<JavaExec>("ktlintFormat") {
        group = "formatting"
        description = "Fix Kotlin code style deviations."
        classpath = configurations["ktlint"]
        main = "com.github.shyiko.ktlint.Main"
        args = listOf("-F", "src/**/*.kt")
    }
}
