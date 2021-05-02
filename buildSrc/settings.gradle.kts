enableFeaturePreview("VERSION_CATALOGS")

rootProject.buildFileName = "buildSrc.gradle.kts"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
