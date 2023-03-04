enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

includeBuild("gradle-build-logic")

rootProject.name = "TonSkanner"

include(":common:lib:entryfield")
include(":common:lib:resources")
include(":common:lib:state")
include(":common:lib:validation")

include(":common:core:storage")
include(":common:core:ton")

include(":common:features:base")
include(":common:features:component:blockchain:addressinfo")
include(":common:features:component:blockchain:entity")
include(":common:features:component:blockchain:transactions")
include(":common:features:component:inputaddress")
include(":common:features:component:queryhistory")
include(":common:features:screen:explorermain")
include(":common:features:screen:startexplorer")
include(":common:features:screen:transactionlist")
include(":common:root")

include(":common:compose-ui")

include(":app-android")
include(":app-desktop")
