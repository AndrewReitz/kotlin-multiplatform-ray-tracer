import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.0-rc-57"
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
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
    }
}

subprojects {
    apply(plugin = "kotlin")

    configurations.create("ktlint")

    dependencies {
        api(kotlin("stdlib"))
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.26.1-eap13")

//        api("com.google.dagger:dagger:2.17")
//        kapt("com.google.dagger:dagger-compiler:2.17")

        testImplementation(kotlin("test"))
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")

        add("ktlint", "com.github.shyiko:ktlint:0.28.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
        }
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
