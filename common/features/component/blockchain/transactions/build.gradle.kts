plugins {
    id("kmm-module-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    id("io.kotest.multiplatform")
}

group = "ru.tetraquark.ton.explorer.features.component.blockchain.transactions"

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
                implementation(libs.korlibs.klock)

                implementation(libs.mvikotlin.core)
                implementation(libs.mvikotlin.main)
                implementation(libs.mvikotlin.coroutines)

                implementation(project(":common:lib:state"))
                implementation(project(":common:core:ton"))
                implementation(project(":common:features:base"))
                api(project(":common:features:component:blockchain:entity"))
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
    multiplatformResourcesClassName = "MRTransactions"
}
