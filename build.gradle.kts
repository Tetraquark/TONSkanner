buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath(":gradle-build-logic")
        classpath(libs.kotlinx.serialization.gradle)
        classpath(libs.moko.resources.gradle)
        classpath(libs.kotest.framework.gradle)
    }
}

allprojects {
    group = "ru.tetraquark.ton.explorer"
    version = "0.0.1"

    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
}
