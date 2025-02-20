plugins {
    alias(libs.plugins.hijrahDatetime.library)
    alias(libs.plugins.hijrahDatetime.publish)
}

hijrahDateTimePublishing {
    artifactName = "HijrahDateTime"
    artifactDescription = rootProject.description
    artifactId = rootProject.name
}

sourceSets {
    test {
        java {
            srcDir("src/test/java")
        }

        kotlin {
            srcDir("src/test/kotlin")
        }
    }
}

dependencies {
    testImplementation(libs.threeTenExtra)
}