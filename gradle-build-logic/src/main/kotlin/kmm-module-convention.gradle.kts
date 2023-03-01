plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("android-module-convention")
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val androidMain by getting
        val androidUnitTest by getting
        val desktopMain by getting
        val desktopTest by getting
    }
}
