plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("com.android.tools.build:gradle:3.6.3")
    implementation("com.google.gms:google-services:4.3.3")
    implementation("com.google.firebase:firebase-crashlytics-gradle:2.0.0")
    implementation("com.squareup:kotlinpoet:1.5.0")
}
