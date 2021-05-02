enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
  "raytracer-core",
  "raytracer-math",
  "raytracer-math-test",
  "raytracer-console",
  "raytracer-console-jvm",
  "raytracer-scene-builder",
  "raytracer-serialization"
)

rootProject.name = "kotlin-raytracer"

for (project in rootProject.children) {
  project.apply {
    buildFileName = "${name.replace("raytracer-", "")}.gradle.kts"
  }
}
