plugins {
  id("com.github.ben-manes.versions") version "0.36.0"
  id("org.jmailen.kotlinter") version "3.2.0"
}

subprojects {
  apply(plugin = "org.jmailen.kotlinter")

  repositories {
    mavenCentral()
    jcenter()
  }
}
