include(
        "raytracer-core",
        "raytracer-math",
        "raytracer-console",
        "raytracer-parsing"
)

rootProject.name = "kotlin-raytracer"

for (project in rootProject.children) {
    project.apply {
        buildFileName = "${name.replace("raytracer-", "")}.gradle.kts"
        assert(projectDir.isDirectory)
        assert(buildFile.isFile)
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap") }
    }
}
