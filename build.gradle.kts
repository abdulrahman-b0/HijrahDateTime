@file:Suppress("MemberVisibilityCanBePrivate", "unused")


plugins {
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

rootProject.group = "com.abdulrahman-b.hijrahdatetime"
rootProject.version = "2.0.0-alpha01"
rootProject.description =
    "HijrahDateTime is a Kotlin Multiplatform library for working with the Hijrah calendar system. It provides a robust set of classes and functions to handle Hijrah dates and times across different platforms, integrating seamlessly with `kotlinx-datetime`."

