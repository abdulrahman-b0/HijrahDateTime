@file:Suppress("UnstableApiUsage")

rootProject.name = "HijrahDateTime"

includeBuild("build-logic")
include(":core")
include(":serialization")
include(":serialization:kotlinx")
include(":serialization:jackson")
include(":jakarta-validation")

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
