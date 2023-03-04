plugins {
    id("kmm-module-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("io.kotest.multiplatform")
}

group = "ru.tetraquark.ton.explorer.features.screen.explorermain"

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

                implementation(projects.common.lib.entryfield)
                implementation(projects.common.lib.state)
                implementation(projects.common.core.ton)
                implementation(projects.common.core.storage)
                implementation(projects.common.features.base)
                implementation(projects.common.features.component.blockchain.addressinfo)
                implementation(projects.common.features.component.blockchain.entity)
                implementation(projects.common.features.component.inputaddress)
                implementation(projects.common.features.component.blockchain.transactions)
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
    multiplatformResourcesClassName = "MRExplorerMain"
}
