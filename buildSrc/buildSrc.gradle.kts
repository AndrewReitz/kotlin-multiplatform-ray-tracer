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
    implementation(kotlin("gradle-plugin", "1.4.0"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    implementation("com.android.tools.build:gradle:4.0.1")
    implementation("com.google.gms:google-services:4.3.3")
    implementation("com.google.firebase:firebase-crashlytics-gradle:2.2.0")
    implementation("com.squareup:kotlinpoet:1.5.0")

    testImplementation(gradleKotlinDsl())
    testImplementation(gradleTestKit())
    implementation(kotlin("test-junit", "1.4.0"))
    testImplementation("junit:junit:4.13")
}
