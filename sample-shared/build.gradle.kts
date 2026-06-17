
plugins {
    alias(libs.plugins.hijrahdatetime.multiplatform.compose.library)
}

kotlin {

    android.namespace = "com.abdulrahman_b.hijrahdatetime.sample.shared"

    sourceSets {
        commonMain.dependencies {
            implementation(projects.composePickers)
        }
    }

}

dependencies {
    androidRuntimeClasspath(libs.composeMultiplatform.ui.tooling.asProvider())
}

