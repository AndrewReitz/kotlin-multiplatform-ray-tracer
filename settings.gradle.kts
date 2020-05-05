include(
  ":trail-conditions-app",
  ":trail-conditions-networking",
  ":trail-conditions-firebase"
)

rootProject.name = "mn-trail-conditions"

rootProject.children.forEach {
  it.buildFileName = "${it.name.replace("trail-conditions-", "")}.gradle.kts"
}

plugins {
  id("com.gradle.enterprise") version "3.2.1"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlways()
  }
}
