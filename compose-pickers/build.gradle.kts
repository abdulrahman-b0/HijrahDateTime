plugins {
    alias(libs.plugins.hijrahdatetime.multiplatform.compose.library)
    alias(libs.plugins.hijrahdatetime.publish)
}

kotlin {

    android.namespace = "com.abdulrahman_b.hijrahdatetime.pickers"

    sourceSets {
        commonMain.dependencies {
            api(projects.core)
        }
        jvmCommonMain
    }

}

compose {
    resources {
        publicResClass = false
        packageOfResClass = "com.abdulrahman_b.hijrahdatetime.resources"
    }
}

dependencies {
    androidRuntimeClasspath(libs.composeMultiplatform.ui.tooling.asProvider())
}

hijrahDateTimePublishing {
    mavenCoordinates(
        artifactId = "hijrahdatetime-compose-pickers",
        artifactName = "HijrahDateTime Compose Pickers"
    )
}

