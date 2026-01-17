@file:Suppress("UnstableApiUsage")

include(":library")


rootProject.name = "hijrahdatetime"

includeBuild("build-logic")
include(":core")
include(":serialization")
include(":serialization:kotlinx")
include(":serialization:jackson")
include(":jakarta-validation")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
