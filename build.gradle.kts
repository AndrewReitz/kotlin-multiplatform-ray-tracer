import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application") version "3.3.0-alpha10"
    id("kotlin-android") version "1.2.60"
    id("kotlin-kapt") version "1.2.60"
    id("kotlin-android-extensions") version "1.2.60"
    id("io.fabric") version "1.25.4"
    id("androidx.navigation.safeargs") version "1.0.0-alpha05"
    id("com.google.gms.google-services") version "4.0.1"
    id("com.gradle.build-scan") version "1.15.2"
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

android {
    compileSdkVersion(28)
    buildToolsVersion("28.0.2")

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
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
        versionName = "🍦"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
            val alwaysUpdateBuildId by extra { false }
            val enableCrashlytics by extra { false }
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }

    flavorDimensions("environment")

    productFlavors {
        create("internal") {
            dimension = "environment"
            applicationIdSuffix = "internal"
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

    sourceSets {
        getByName("main") { java.srcDirs(file("./src/main/kotlin")) }
        getByName("test") { java.srcDirs(file("src/test/kotlin")) }
        create("internalDebug") { java.srcDirs(file("src/internalDebug/kotlin")) }
        getByName("production") { java.srcDirs(file("src/production/kotlin")) }
    }
}

val stethoVersion by extra { "1.5.0" }
val retrofitVersion by extra { "2.4.0" }

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha2")
    implementation("androidx.annotation:annotation:1.0.0-rc01")
    implementation("androidx.appcompat:appcompat:1.0.0-rc01")
    implementation("androidx.recyclerview:recyclerview:1.0.0-rc01")
    implementation("androidx.cardview:cardview:1.0.0-rc01")
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0-rc01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0-rc01")

    implementation("android.arch.navigation:navigation-fragment-ktx:1.0.0-alpha05")
    implementation("android.arch.navigation:navigation-ui-ktx:1.0.0-alpha05")
    implementation("com.google.android.material:material:1.0.0-rc01")

    implementation("com.google.firebase:firebase-core:16.0.1")
    implementation("com.google.firebase:firebase-perf:16.1.0")
    implementation("com.google.firebase:firebase-config:16.0.0")
    implementation("com.google.firebase:firebase-messaging:17.3.0")

    implementation("com.google.dagger:dagger:2.16")
    kapt("com.google.dagger:dagger-compiler:2.16")

    implementation("com.squareup.okhttp3:okhttp:3.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.10.0")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")

    implementation("com.squareup.moshi:moshi:1.6.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.6.0")

    implementation("com.jakewharton.rxbinding2:rxbinding:2.1.1")
    implementation("com.jakewharton:process-phoenix:2.0.0")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.jakewharton.byteunits:byteunits:0.9.1")

    add("debugImplementation", "com.readystatesoftware.chuck:library:1.1.0")
    add("releaseImplementation", "com.readystatesoftware.chuck:library-no-op:1.1.0")

    add("internalImplementation", "com.squareup.leakcanary:leakcanary-android:1.5.1")
    add("productionImplementation", "com.squareup.leakcanary:leakcanary-android-no-op:1.5.1")

    implementation("io.reactivex.rxjava2:rxjava:2.2.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.2")

    implementation("com.jakewharton.threetenabp:threetenabp:1.1.0")

    implementation("com.f2prateek.rx.preferences2:rx-preferences:2.0.0")

    implementation("io.github.kobakei:ratethisapp:1.2.0")

    add("internalImplementation", "com.facebook.stetho:stetho:$stethoVersion")
    add("internalImplementation", "com.facebook.stetho:stetho-okhttp3:$stethoVersion")
    add("internalImplementation", "com.facebook.stetho:stetho-timber:$stethoVersion@aar")

    implementation("com.crashlytics.sdk.android:crashlytics:2.9.4")

    testImplementation("org.amshove.kluent:kluent-android:1.38")
}

kapt.useBuildCache = true

val installAll: Task = tasks.create("installAll")
installAll.description = "Install all applications."
android.applicationVariants.all {
    installAll.dependsOn(install)
    // Ensure we end up in the same group as the other install tasks.
    installAll.group = install.group
}

// The default "assemble" task only applies to normal variants. Add test variants as well.
android.testVariants.all {
    tasks.getByName("assemble").dependsOn(assemble)
}

tasks.withType<Test> {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}

tasks.withType<JavaCompile> {
    options.isFork = true
}

tasks["lint"].enabled = false