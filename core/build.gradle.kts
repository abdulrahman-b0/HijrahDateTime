plugins {
    alias(libs.plugins.hijrahdatetime.multiplatform.library)
    alias(libs.plugins.hijrahdatetime.publish)
}

kotlin {

    android.namespace = "com.abdulrahman_b.hijrahdatetime"

    sourceSets {
        jvmCommonMain.dependencies {
            implementation(libs.kotlin.datetime.jvm)
        }
    }
}

hijrahDateTimePublishing {
    mavenCoordinates(
        artifactId = "hijrahdatetime",
        artifactName = "HijrahDateTime Core"
    )
}


