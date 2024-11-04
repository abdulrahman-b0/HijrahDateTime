plugins {
    alias(libs.plugins.hijrahDatetime.library)
}

dependencies {
    implementation(projects.core)
    implementation(libs.jakarta.validation.api)
    testImplementation(libs.hibernate.validator)
    testImplementation(libs.expressly)
    testImplementation(libs.mockk)
}