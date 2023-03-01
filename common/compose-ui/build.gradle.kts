plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("android-module-convention")
    id("org.jetbrains.compose")
}

group = "ru.tetraquark.ton.explorer.app.ui"

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                // Needed only for preview.
                implementation(compose.preview)

                api(project(":common:root"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.androidx.appcompat)
                api(libs.androidx.core.ktx)
                api(libs.coil.core)
                api(libs.coil.compose)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(libs.compose.imageloader)
            }
        }
    }
}
