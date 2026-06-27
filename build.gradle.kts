@file:Suppress("MemberVisibilityCanBePrivate", "unused")


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

rootProject.group = "com.abdulrahman-b.hijrahdatetime"
rootProject.version = "2.0.0-alpha07"
rootProject.description =
    "HijrahDateTime is a Kotlin Multiplatform library for the Hijrah calendar system. It provides robust data models and functions for handling Hijrah dates and times, alongside a suite of Compose Multiplatform UI pickers, integrating seamlessly with `kotlinx-datetime`."

