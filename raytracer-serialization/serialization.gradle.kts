description = "Shared code for serializing and deserializing raytracing objects"

plugins {
    id("multiplatform-common")
    kotlin("plugin.serialization")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.raytracerCore)
                api(libs.kotlinx.serialization.json)
            }
        }

        commonTest {
            dependencies {
                implementation(projects.raytracerMathTest)
            }
        }
    }
}
