plugins {
    id("kmm-module-convention")
    kotlin("native.cocoapods")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("dev.icerock.mobile.multiplatform-resources")
    id("io.kotest.multiplatform")
}

group = "ru.tetraquark.ton.explorer.features.root"

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
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                implementation(libs.ton.kotlin)
                implementation(libs.korlibs.kbignum)

                api(libs.esenty.parcelable)
                api(libs.decompose.core)
                api(libs.moko.resources)
                api(libs.multiplatformsettings)

                api(projects.common.lib.entryfield)
                api(projects.common.lib.resources)
                api(projects.common.lib.validation)
                api(projects.common.core.storage)
                api(projects.common.core.ton)
                api(projects.common.features.base)
                api(projects.common.features.component.blockchain.addressinfo)
                api(projects.common.features.component.blockchain.entity)
                api(projects.common.features.component.blockchain.transactions)
                api(projects.common.features.component.inputaddress)
                api(projects.common.features.component.queryhistory)
                api(projects.common.features.screen.explorermain)
                api(projects.common.features.screen.startexplorer)
                api(projects.common.features.screen.transactionlist)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.assertions.core)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.moko.resources.compose)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                //implementation("junit:junit:4.13.2")
                //implementation(libs.kotest.runner.junit5)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(libs.moko.resources.compose)
            }
        }
        val desktopTest by getting {
            dependencies {
                //implementation("junit:junit:4.13.2")
                implementation(libs.kotest.runner.junit5)
            }
        }
    }

    cocoapods {
        version = "0.0.1"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        name = "common"

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "RootFramework"
            isStatic = false

            export(libs.esenty.parcelable)
            export(libs.esenty.lifecycle)
            export(libs.esenty.statekeeper)
            export(libs.decompose.core)
            export(libs.moko.resources)
            export(libs.multiplatformsettings)
            export(libs.korlibs.kbignum)
            export(projects.common.lib.entryfield)
            export(projects.common.lib.resources)
            export(projects.common.lib.state)
            export(projects.common.lib.validation)
            export(projects.common.core.storage)
            export(projects.common.core.ton)
            export(projects.common.features.base)
            export(projects.common.features.component.blockchain.addressinfo)
            export(projects.common.features.component.blockchain.entity)
            export(projects.common.features.component.blockchain.transactions)
            export(projects.common.features.component.inputaddress)
            export(projects.common.features.component.queryhistory)
            export(projects.common.features.screen.explorermain)
            export(projects.common.features.screen.startexplorer)
            export(projects.common.features.screen.transactionlist)
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "ru.tetraquark.ton.explorer.features.root"
}
