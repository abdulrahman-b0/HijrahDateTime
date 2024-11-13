@file:Suppress("MemberVisibilityCanBePrivate", "unused")


plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.kover)
}

rootProject.group = "com.abdulrahman-b"
rootProject.version = "1.0.0-beta.3"
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
        "com.abdulrahman_b.hijrah_datetime.serialization.jackson.HijrahDateSerialization",
        "com.abdulrahman_b.hijrah_datetime.serialization.jackson.HijrahDateTimeSerialization",
        "com.abdulrahman_b.hijrah_datetime.serialization.jackson.OffsetHijrahDateSerialization",
        "com.abdulrahman_b.hijrah_datetime.serialization.jackson.OffsetHijrahDateTimeSerialization",
        "com.abdulrahman_b.hijrah_datetime.serialization.jackson.ZonedHijrahDateTimeSerialization",
    )
}

