@file:Suppress("UnstableApiUsage")

include(":core")


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
