plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

    id("com.github.triplet.play") version "2.7.5"

    id("build-number")
    id("android-signing-config")
}

repositories {
    mavenCentral()
    google()
    jcenter {
        content {
            includeGroup("org.amshove.kluent")
            includeGroup("org.jetbrains.trove4j")
        }
    }
    maven {
        url = uri("https://jitpack.io")
        content {
            includeGroup("com.github.AndrewReitz.andrew-kotlin-commons")
        }
    }
}

play {
    serviceAccountCredentials = file(properties["cash.andrew.mntrail.publishKey"] ?: "keys/publish-key.json")
    track = "internal"
    defaultToAppBundles = true
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    signingConfigs {
        getByName("debug") {
            storeFile = file("keys/debug.keystore")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
        create("release") {
            val keystoreLocation: String by project
            val keystorePassword: String by project
            val storeKeyAlias: String by project
            val aliasKeyPassword: String by project

            storeFile = file(keystoreLocation)
            storePassword = keystorePassword
            keyAlias = storeKeyAlias
            keyPassword = aliasKeyPassword
        }
    }

    defaultConfig {
        applicationId = "com.andrewreitz.cash.andrew.mntrailconditions"
        minSdkVersion(23)
        targetSdkVersion(29)

        val buildNumber: String by project
        versionCode = buildNumber.toInt()
        versionName = "三"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
            extra["alwaysUpdateBuildId"] = false
            extra["enableCrashlytics"] = false
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("LICENSE.txt")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
        exclude("META-INF/ktor*")
    }

    viewBinding {
        isEnabled = true
    }
}

dependencies {

    implementation(project(":trail-conditions-networking"))
    implementation("io.ktor:ktor-client-core:${versions.ktor}")
    implementation("io.ktor:ktor-client-logging:${versions.ktor}")
    implementation("io.ktor:ktor-client-okhttp:${versions.ktor}")

    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")

    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")


    implementation("android.arch.navigation:navigation-fragment-ktx:1.0.0")
    implementation("android.arch.navigation:navigation-ui-ktx:1.0.0")
    implementation("com.google.android.material:material:1.1.0")

    implementation("com.google.firebase:firebase-core:17.4.0")
    implementation("com.google.firebase:firebase-messaging:20.1.6")
    implementation("com.google.firebase:firebase-analytics:17.4.0")
    implementation("com.google.firebase:firebase-crashlytics:17.0.0")

    debugImplementation("com.jakewharton.dagger:dagger-reflect:0.2.0")

    kaptRelease("com.google.dagger:dagger-compiler:2.27")
    implementation("com.google.dagger:dagger:2.27")

    implementation("com.squareup.okhttp3:okhttp:4.5.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.5.0")

    val retrofitVersion = "2.8.1"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")

    implementation("com.squareup.moshi:moshi:1.9.2")
    debugImplementation("com.squareup.moshi:moshi-kotlin:1.9.2")
    kaptRelease("com.squareup.moshi:moshi-kotlin-codegen:1.9.2")

    implementation("com.jakewharton.threetenabp:threetenabp:1.2.3")
    implementation("com.jakewharton:process-phoenix:2.0.0")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.jakewharton.byteunits:byteunits:0.9.1")

    debugImplementation("com.readystatesoftware.chuck:library:1.1.0")
    releaseImplementation("com.readystatesoftware.chuck:library-no-op:1.1.0")

    implementation("com.github.AndrewReitz.andrew-kotlin-commons:andrew-kotlin-commons:master-SNAPSHOT")
    implementation("io.noties.markwon:core:4.3.1")

    val stethoVersion = "1.5.1"
    debugImplementation("com.facebook.stetho:stetho:$stethoVersion")
    debugImplementation("com.facebook.stetho:stetho-okhttp3:$stethoVersion")
    debugImplementation("com.facebook.stetho:stetho-timber:$stethoVersion@aar")

    testImplementation("org.amshove.kluent:kluent-android:1.61")
    testImplementation("junit:junit:4.13")
}

val installAll = tasks.register("installAll") {
    description = "Install all applications."
    group = "install"
    dependsOn(android.applicationVariants.map { it.installProvider })
}
