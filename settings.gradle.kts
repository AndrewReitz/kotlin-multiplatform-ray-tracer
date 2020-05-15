include(
  "raytracer-core",
  "raytracer-math",
  "raytracer-math-test",
  "raytracer-console-jvm",
  "raytracer-console"
)

rootProject.name = "kotlin-raytracer"

for (project in rootProject.children) {
  project.apply {
    buildFileName = "${name.replace("raytracer-", "")}.gradle.kts"
  }
}

plugins {
  id("com.gradle.enterprise").version("3.3")
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}
