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
  implementation(kotlin("stdlib", "1.3.72"))
  implementation(kotlin("gradle-plugin", "1.3.72"))
}
