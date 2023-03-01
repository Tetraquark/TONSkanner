plugins {
    `kotlin-dsl`
}

repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    api(libs.kotlin.gradle)
    api(libs.android.gradle)
    api(libs.compose.jetbrains.gradle)
}
