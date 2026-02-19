plugins {
    alias(libs.plugins.hijrahDatetime.library)
    alias(libs.plugins.hijrahDatetime.publish)
}

hijrahDateTimePublishing {
    artifactName = "HijrahDateTime Jakarta Validation"
    artifactDescription = "A library that provides Jakarta Validation support for HijrahDateTime library."
    artifactId = "${rootProject.name}-jakarta-validation"
}

dependencies {
    implementation(projects.core)
    implementation(libs.jakarta.validation.api)
    testImplementation(libs.hibernate.validator)
    testImplementation(libs.expressly)
    testImplementation(libs.mockk)
}