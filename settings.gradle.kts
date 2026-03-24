@file:Suppress("UnstableApiUsage")

include(":library")


rootProject.name = "hijrahdatetime"

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
        google()
    }

}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
