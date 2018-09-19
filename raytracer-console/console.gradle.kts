plugins {
    application
}

application {
    mainClassName = "raytracer.console.MainKt"
}

dependencies {
    compile(project(":raytracer-core"))
}
