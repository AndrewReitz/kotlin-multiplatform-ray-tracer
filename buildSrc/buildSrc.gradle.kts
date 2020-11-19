plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  jcenter()
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

dependencies {
  implementation(kotlin("stdlib", "1.4.20"))
  implementation(kotlin("gradle-plugin", "1.4.20"))
}
