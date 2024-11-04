plugins {
    alias(libs.plugins.hijrahDatetime.library)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.jackson.core)
    implementation(libs.jackson.core.annotations)
    implementation(libs.jackson.core.databind)
    implementation(libs.jackson.module.kotlin)
}
