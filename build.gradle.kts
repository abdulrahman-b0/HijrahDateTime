import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")
}

group = "com.abdulrahman-b"
version = "1.0-alpha01"

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.serialization.json)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}