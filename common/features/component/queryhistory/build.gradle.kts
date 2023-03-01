plugins {
    id("kmm-module-convention")
    id("io.kotest.multiplatform")
}

group = "ru.tetraquark.ton.explorer.features.component.queryhistory"

kotlin {
    jvm("desktop") {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.decompose.core)
                implementation(libs.esenty.parcelable)
                implementation(libs.moko.resources)

                implementation(project(":common:core:storage"))
                implementation(project(":common:features:base"))
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
