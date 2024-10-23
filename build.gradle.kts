plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.abdulrahman-b"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.slf4j.api)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
}

tasks.test {
    useJUnitPlatform()
}