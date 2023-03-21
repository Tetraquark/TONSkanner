plugins {
    id("kmm-module-convention")
    id("io.kotest.multiplatform")
}

group = "ru.tetraquark.ton.explorer.features.base"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.decompose.core)
                implementation(libs.moko.resources)

                implementation(projects.common.lib.entryfield)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.assertions.core)
            }
        }
    }
}
