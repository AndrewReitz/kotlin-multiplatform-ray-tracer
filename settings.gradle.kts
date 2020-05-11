include(
  "raytracer-core",
  "raytracer-math",
  "raytracer-console-jvm",
  "raytracer-console"
)

rootProject.name = "kotlin-raytracer"

for (project in rootProject.children) {
  project.apply {
    buildFileName = "${name.replace("raytracer-", "")}.gradle.kts"
  }
}
