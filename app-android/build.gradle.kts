plugins {
    id("com.android.application")
    id("android-module-convention")
    kotlin("android")
    id("org.jetbrains.compose")
}

group = "ru.tetraquark.ton.explorer.app.android"
version = "0.0.1"

android {
    defaultConfig {
        applicationId = "ru.tetraquark.ton.explorer.app.android"

        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        // Multiple dependency bring these files in. Exclude them to enable
        // our test APK to build (has no effect on our AARs)
        excludes += "META-INF/*.kotlin_module"
        excludes += "/META-INF/AL2.0"
        excludes += "/META-INF/LGPL2.1"
    }
}

dependencies {
    implementation(project(":common:compose-ui"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.ktor.okhttp)
    implementation(libs.decompose.ext.jetpack)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
//    implementation(libs.androidx.compose.uiToolingPreview)
//    debugImplementation(libs.androidx.compose.uiTooling)
}
