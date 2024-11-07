
plugins {
    alias(libs.plugins.hijrahDatetime.library)
    alias(libs.plugins.hijrahDatetime.publish)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.core)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.serialization.json)
}

hijrahDateTimePublishing {
    artifactId = "${rootProject.name}-serialization-kotlinx"
}
