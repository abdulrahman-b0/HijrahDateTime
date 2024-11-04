
plugins {
    alias(libs.plugins.hijrahDatetime.library)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.serialization.json)
}
