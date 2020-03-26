import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    val kotlinVersion = "1.3.71"
    id("com.android.application") version "3.4.0"
    id("kotlin-android") version kotlinVersion
    id("kotlin-kapt") version kotlinVersion
    id("kotlin-android-extensions") version kotlinVersion
    id("io.fabric") version "1.31.2"
    id("com.github.triplet.play") version "2.2.0"
    id("com.github.ben-manes.versions") version "0.28.0"

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

play {
    serviceAccountCredentials = file(properties["cash.andrew.mntrail.publishKey"]
            ?: "keys/publish-key.json")
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
        minSdkVersion(21)
        targetSdkVersion(29)

        val buildNumber: String by project
        versionCode = if (buildNumber.isBlank()) 1 else buildNumber.toInt()
        versionName = "🍈"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
            extra["alwaysUpdateBuildId"] = false
            extra["enableCrashlytics"] = false

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
            setDimension("environment")
            applicationIdSuffix = ".internal"
        }
        create("production") { setDimension("environment") }
    }

    variantFilter {
        val names = flavors.map { it.name }
        if (buildType.name == "release" && "internal" in names) {
            setIgnore(true)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("LICENSE.txt")
        exclude("META-INF/LICENSE.txt")
    }

    sourceSets.all {
        java.srcDirs(file("src/$name/kotlin"))
    }
}

val stethoVersion by extra("1.5.1")
val retrofitVersion by extra("2.7.1")
val autoDisposeVersion by extra("1.2.0")

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))

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

    implementation("com.google.firebase:firebase-core:17.2.3")
    implementation("com.google.firebase:firebase-messaging:20.1.3")

    implementation("com.google.dagger:dagger:2.27")
    kapt("com.google.dagger:dagger-compiler:2.27")

    implementation("com.squareup.okhttp3:okhttp:4.4.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.4.0")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")

    implementation("com.squareup.moshi:moshi:1.9.2")
    debugImplementation("com.squareup.moshi:moshi-kotlin:1.9.2")
    kaptRelease("com.squareup.moshi:moshi-kotlin-codegen:1.9.2")

    implementation("com.jakewharton.rxbinding2:rxbinding:2.2.0")
    implementation("com.jakewharton:process-phoenix:2.0.0")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.jakewharton.byteunits:byteunits:0.9.1")

    debugImplementation("com.readystatesoftware.chuck:library:1.1.0")
    releaseImplementation("com.readystatesoftware.chuck:library-no-op:1.1.0")

    implementation("io.reactivex.rxjava2:rxjava:2.2.19")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation("com.uber.autodispose:autodispose-ktx:$autoDisposeVersion")
    implementation("com.uber.autodispose:autodispose-android:$autoDisposeVersion")
    implementation("com.uber.autodispose:autodispose-android-ktx:$autoDisposeVersion")
    implementation("com.uber.autodispose:autodispose-ktx:$autoDisposeVersion")
    implementation("com.uber.autodispose:autodispose-android-archcomponents-ktx:$autoDisposeVersion")

    implementation("com.jakewharton.threetenabp:threetenabp:1.2.2")

    implementation("com.f2prateek.rx.preferences2:rx-preferences:2.0.0")

    implementation("io.github.kobakei:ratethisapp:1.2.0")

    "internalImplementation"("com.facebook.stetho:stetho:$stethoVersion")
    "internalImplementation"("com.facebook.stetho:stetho-okhttp3:$stethoVersion")
    "internalImplementation"("com.facebook.stetho:stetho-timber:$stethoVersion@aar")

    implementation("com.crashlytics.sdk.android:crashlytics:2.10.1")

    testImplementation("org.amshove.kluent:kluent-android:1.60")
    testImplementation("junit:junit:4.13")
}

kapt {
    useBuildCache = true

    arguments {
        arg("dagger.formatGeneratedSource", "disabled")
    }
}

val installAll = tasks.register("installAll") {
    description = "Install all applications."
    group = "install"
}
android.applicationVariants.all {
    installAll.dependsOn(installProvider)
}

tasks.named("lint").configure {
    enabled = properties["runLint"] == "true"
}
