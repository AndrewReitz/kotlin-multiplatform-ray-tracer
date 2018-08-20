rootProject.name = "mn-trail-info-app"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        maven { url = uri("https://maven.fabric.io/public") }
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id in arrayOf("kotlin-android", "kotlin-kapt", "kotlin-android-extensions")) {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }

            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }

            if (requested.id.id == "com.google.gms.google-services") {
                useModule("com.google.gms:google-services:${requested.version}")
            }

            if (requested.id.id == "io.fabric") {
                useModule("io.fabric.tools:gradle:${requested.version}")
            }

            if (requested.id.id == "com.google.firebase.firebase-perf") {
                useModule("com.google.firebase:firebase-plugins:${requested.version}")
            }

            if (requested.id.id == "androidx.navigation.safeargs") {
                useModule("android.arch.navigation:navigation-safe-args-gradle-plugin:${requested.version}")
            }
        }
    }
}
