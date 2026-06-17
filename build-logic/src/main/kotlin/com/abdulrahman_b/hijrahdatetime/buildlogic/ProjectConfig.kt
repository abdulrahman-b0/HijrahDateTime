package com.abdulrahman_b.hijrahdatetime.buildlogic

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


/**
 * Defines configuration constants for the Android project.
 *
 * This object contains constants used for configuring the project build, such as minimum SDK
 * version, target SDK version, compile SDK version, Java version, and other relevant parameters.
 */
object ProjectConfig {

    const val ANDROID_MIN_SDK = 26
    const val ANDROID_COMPILE_SDK = 37
    const val ANDROID_TARGET_SDK = 37
    val ANDROID_JAVA_VERSION = JavaVersion.VERSION_11
    val ANDROID_JVM_TARGET = JvmTarget.JVM_11
    val DESKTOP_JVM_TARGET = JvmTarget.JVM_11
    const val ANDROID_TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
}