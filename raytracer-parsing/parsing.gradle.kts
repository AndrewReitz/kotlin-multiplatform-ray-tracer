plugins {
    `java-library`
}

dependencies {
    api(project(":raytracer-core"))
    implementation("com.squareup.moshi:moshi:1.7.0")
}