rootProject.name = "mn-trail-info-app"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {

            val artifact = when(requested.id.id) {
                "com.android.application" -> "com.android.tools.build:gradle:"
                "com.google.firebase.crashlytics" -> "com.google.firebase:firebase-crashlytics-gradle:"
                "androidx.navigation.safeargs" -> "android.arch.navigation:navigation-safe-args-gradle-plugin:"
                "com.github.triplet.play" -> "com.github.triplet.gradle:play-publisher:"
                "com.google.gms.google-services" -> "com.google.gms:google-services:"
                in arrayOf("kotlin-android", "kotlin-kapt", "kotlin-android-extensions") -> "org.jetbrains.kotlin:kotlin-gradle-plugin:"
                else -> return@eachPlugin
            } + requested.version

            useModule(artifact)
        }
    }
}

plugins {
    id("com.gradle.enterprise").version("3.1.1")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}
