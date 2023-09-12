plugins {
    id("kmm-module-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("org.jetbrains.compose")
}

group = "ru.tetraquark.ton.explorer.app.ui"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(libs.compose.imageloader)
                implementation(libs.moko.resources)
                implementation(libs.moko.resources.compose)
                // Needed only for preview.
                //implementation(compose.preview)

                api(projects.common.root)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.core.ktx)
                implementation(libs.coil.core)
                implementation(libs.coil.compose)
            }
        }
        val desktopMain by getting {
            dependencies {
                //implementation(libs.compose.imageloader)
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "ru.tetraquark.ton.explorer.app.ui"
}
