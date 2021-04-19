import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("main-kts"))
    implementation(kotlin("compiler-embeddable"))
    implementation(libs.clikt)
    implementation(projects.raytracerCore)
    implementation(projects.raytracerSerialization)
}

application {
    mainClass.set("raytracer.scene.MainKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xno-param-assertions", "-Xno-call-assertions")
        jvmTarget = "11"
        useIR = true
    }
}