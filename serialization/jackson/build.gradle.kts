plugins {
    alias(libs.plugins.hijrahDatetime.library)
    alias(libs.plugins.hijrahDatetime.publish)
}

hijrahDateTimePublishing {
    artifactId = "${rootProject.name}-serialization-jackson"
}

dependencies {
    implementation(projects.core)
    implementation(libs.jackson.core)
    implementation(libs.jackson.core.annotations)
    implementation(libs.jackson.core.databind)
    implementation(libs.jackson.module.kotlin)
}
