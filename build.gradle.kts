import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    val kotlinVersion = "1.3.21"
    id("com.android.application") version "3.3.1"
    id("kotlin-android") version kotlinVersion
    id("kotlin-kapt") version kotlinVersion
    id("kotlin-android-extensions") version kotlinVersion
    id("io.fabric") version "1.27.1"
    id("com.gradle.build-scan") version "2.1"
    id("com.github.triplet.play") version "2.1.0"
    id("com.github.ben-manes.versions") version "0.20.0"

    // this is broken...
    // values have just been added by hand now.
    // id("com.google.gms.google-services") version "4.2.0"
}

apply(from = "$rootDir/gradle/signing.gradle.kts")
apply(from = "$rootDir/gradle/buildNumber.gradle.kts")

repositories {
    google()
    jcenter()
    maven { url = uri("https://maven.fabric.io/public") }
    maven { url = uri("https://jitpack.io") }
}

buildScan {
    setTermsOfServiceUrl("https://gradle.com/terms-of-service")
    setTermsOfServiceAgree("yes")
    publishAlways()
}

play {
    serviceAccountCredentials = file(properties["cash.andrew.mntrail.publishKey"] ?: "keys/publish-key.json")
    track = "internal"
    defaultToAppBundles = true
}

android {
    compileSdkVersion(28)
    buildToolsVersion("28.0.3")

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
        minSdkVersion(21)
        targetSdkVersion(28)

        val buildNumber: String by project
        versionCode = if (buildNumber.isBlank()) 1 else buildNumber.toInt()
        versionName = "🍋"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
            val alwaysUpdateBuildId by extra { false }
            val enableCrashlytics by extra { false }

            buildConfigField("boolean", "MOSHI_GENERATOR_ENABLED", "false")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")

            buildConfigField("boolean", "MOSHI_GENERATOR_ENABLED", "true")
        }
    }

    flavorDimensions("environment")

    productFlavors {
        create("internal") {
            dimension = "environment"
            applicationIdSuffix = ".internal"
        }
        create("production") { dimension = "environment" }
    }

    variantFilter {
        val names = flavors.map { it.name }
        if (buildType.name == "release" && "internal" in names) {
            setIgnore(true)
        }
    }

    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }

    packagingOptions {
        exclude("LICENSE.txt")
        exclude("META-INF/LICENSE.txt")
    }

    sourceSets.forEach { sourceSet ->
        sourceSet.java.srcDirs(file("./src/${sourceSet.name}/kotlin"))
    }
}

val stethoVersion by extra { "1.5.0" }
val retrofitVersion by extra { "2.5.0" }
val autoDisposeVersion by extra { "1.1.0" }

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

    implementation("androidx.core:core-ktx:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha3")
    implementation("androidx.annotation:annotation:1.0.1")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0")

    implementation("android.arch.navigation:navigation-fragment-ktx:1.0.0-beta02")
    implementation("android.arch.navigation:navigation-ui-ktx:1.0.0-beta02")
    implementation("com.google.android.material:material:1.0.0")

    implementation("com.google.firebase:firebase-core:16.0.7")
    implementation("com.google.firebase:firebase-messaging:17.3.4")

    implementation("com.google.dagger:dagger:2.21")
    kapt("com.google.dagger:dagger-compiler:2.21")

    implementation("com.squareup.okhttp3:okhttp:3.13.1")
    implementation("com.squareup.okhttp3:logging-interceptor:3.13.1")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")

    implementation("com.squareup.moshi:moshi:1.8.0")
    debugImplementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    kaptRelease("com.squareup.moshi:moshi-kotlin-codegen:1.8.0")

    implementation("com.jakewharton.rxbinding2:rxbinding:2.2.0")
    implementation("com.jakewharton:process-phoenix:2.0.0")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.jakewharton.byteunits:byteunits:0.9.1")

    debugImplementation("com.readystatesoftware.chuck:library:1.1.0")
    releaseImplementation("com.readystatesoftware.chuck:library-no-op:1.1.0")

    add("internalImplementation", "com.squareup.leakcanary:leakcanary-android:1.6.3")
    add("productionImplementation", "com.squareup.leakcanary:leakcanary-android-no-op:1.6.3")

    implementation("io.reactivex.rxjava2:rxjava:2.2.6")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation("com.uber.autodispose:autodispose-ktx:$autoDisposeVersion")
    implementation("com.uber.autodispose:autodispose-android:$autoDisposeVersion")
    implementation("com.uber.autodispose:autodispose-android-ktx:$autoDisposeVersion")
    implementation("com.uber.autodispose:autodispose-ktx:$autoDisposeVersion")
    implementation("com.uber.autodispose:autodispose-android-archcomponents-ktx:$autoDisposeVersion")

    implementation("com.jakewharton.threetenabp:threetenabp:1.1.2")

    implementation("com.f2prateek.rx.preferences2:rx-preferences:2.0.0")

    implementation("io.github.kobakei:ratethisapp:1.2.0")

    add("internalImplementation", "com.facebook.stetho:stetho:$stethoVersion")
    add("internalImplementation", "com.facebook.stetho:stetho-okhttp3:$stethoVersion")
    add("internalImplementation", "com.facebook.stetho:stetho-timber:$stethoVersion@aar")

    implementation("com.crashlytics.sdk.android:crashlytics:2.9.9")

    testImplementation("org.amshove.kluent:kluent-android:1.48")
    testImplementation("junit:junit:4.12")
}

kapt.useBuildCache = true

val installAll: Task = tasks.create("installAll")
installAll.description = "Install all applications."
android.applicationVariants.all {
    installAll.dependsOn(installProvider)
    // Ensure we end up in the same group as the other install tasks.
    installAll.group = "install"
}

tasks["lint"].enabled = properties["runLint"] == "true"
