@file:Suppress("UnstableApiUsage")

include(":jakarta-validators")


rootProject.name = "HijrahDateTime"

includeBuild("build-logic")
include(":core")
include(":serialization")
include(":serialization:kotlinx")
include(":serialization:jackson")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }

}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
