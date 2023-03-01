plugins {
    id("kmm-module-convention")
    id("kotlinx-serialization")
    id("io.kotest.multiplatform")
}

group = "ru.tetraquark.ton.explorer.core.storage"

kotlin {
    jvm("desktop") {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                api(libs.multiplatformsettings)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.assertions.core)
            }
        }
        val androidMain by getting
        val androidUnitTest by getting
        val desktopMain by getting
    }
}
