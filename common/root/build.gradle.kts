plugins {
    id("kmm-module-convention")
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

                api(project(":common:lib:entryfield"))
                api(project(":common:lib:resources"))
                implementation(project(":common:lib:validation"))
                implementation(project(":common:core:storage"))
                api(project(":common:core:ton"))
                implementation(project(":common:features:base"))
                api(project(":common:features:component:blockchain:addressinfo"))
                api(project(":common:features:component:queryhistory"))
                api(project(":common:features:component:inputaddress"))
                api(project(":common:features:component:blockchain:transactions"))
                api(project(":common:features:screen:explorermain"))
                api(project(":common:features:screen:startexplorer"))
                api(project(":common:features:screen:transactionlist"))
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
}

multiplatformResources {
    multiplatformResourcesPackage = "ru.tetraquark.ton.explorer.features.root"
}
