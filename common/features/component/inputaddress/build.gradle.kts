plugins {
    id("kmm-module-convention")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-resources")
    id("io.kotest.multiplatform")
}

group = "ru.tetraquark.ton.explorer.features.component.inputaddress"

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

                implementation(projects.common.lib.entryfield)
                implementation(projects.common.lib.validation)
                implementation(projects.common.core.storage)
                implementation(projects.common.features.base)
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

multiplatformResources {
    multiplatformResourcesPackage = group.toString()
    multiplatformResourcesClassName = "MRInputAddress"
}

