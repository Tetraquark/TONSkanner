plugins {
    id("kmm-module-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("io.kotest.multiplatform")
}

group = "ru.tetraquark.ton.explorer.features.screen.startexplorer"

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
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.decompose.core)
                implementation(libs.esenty.parcelable)
                implementation(libs.moko.resources)

                implementation(project(":common:lib:entryfield"))
                implementation(project(":common:lib:state"))
                implementation(project(":common:core:storage"))
                implementation(project(":common:core:ton"))
                implementation(project(":common:features:base"))
                implementation(project(":common:features:component:queryhistory"))
                implementation(project(":common:features:component:inputaddress"))
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
    multiplatformResourcesClassName = "MRStartExplorer"
}

