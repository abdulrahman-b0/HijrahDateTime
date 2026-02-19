plugins {
    alias(libs.plugins.hijrahDatetime.library)
    alias(libs.plugins.hijrahDatetime.publish)
}

hijrahDateTimePublishing {
    artifactName = "HijrahDateTime Jackson Serialization"
    artifactDescription = "A library that provides Jackson serialization support for HijrahDateTime library."
    artifactId = "${rootProject.name}-serialization-jackson"
}

dependencies {
    implementation(projects.core)
    implementation(libs.jackson.core)
    implementation(libs.jackson.core.databind)
    testImplementation(libs.jackson.core.annotations)
    testImplementation(libs.jackson.module.kotlin)
}
