plugins {
    alias(libs.plugins.hijrahDatetime.library)
    alias(libs.plugins.hijrahDatetime.publish)
}

hijrahDateTimePublishing {
    artifactId = rootProject.name
}

sourceSets {
    test {
        java {
            srcDir("src/test/kotlin")
        }

        kotlin {
            srcDir("src/test/java")
        }
    }
}

dependencies {
    testImplementation(libs.threeTenExtra)
}