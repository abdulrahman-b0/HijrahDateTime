@file:Suppress("MemberVisibilityCanBePrivate", "unused")


plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.kover)
}

rootProject.group = "com.abdulrahman-b"
rootProject.version = "1.0.1"
rootProject.description =
    "HijrahDateTime is a Kotlin/JVM library that is built on top of java.time to facilitates work with Hijrah date and time APIs."


dependencies {
    kover(projects.core)
    kover(projects.serialization.kotlinx)
    kover(projects.serialization.jackson)
    kover(projects.jakartaValidation)
}

kover {
    //Those classes are only containers for nested classes. For organization purposes.
    reports.filters.excludes.classes(
        "com.abdulrahman_b.hijrahdatetime.serialization.jackson.HijrahDateSerialization",
        "com.abdulrahman_b.hijrahdatetime.serialization.jackson.EarlyHijrahDateSerialization",
        "com.abdulrahman_b.hijrahdatetime.serialization.jackson.HijrahDateTimeSerialization",
        "com.abdulrahman_b.hijrahdatetime.serialization.jackson.OffsetHijrahDateSerialization",
        "com.abdulrahman_b.hijrahdatetime.serialization.jackson.OffsetHijrahDateTimeSerialization",
        "com.abdulrahman_b.hijrahdatetime.serialization.jackson.ZonedHijrahDateTimeSerialization",
    )
}

