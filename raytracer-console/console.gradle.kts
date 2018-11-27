plugins {
    application
}

application {
    mainClassName = "raytracer.console.MainKt"
}

dependencies {
    implementation(project(":raytracer-core"))
}
