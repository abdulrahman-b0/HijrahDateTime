plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.api)
}

tasks.withType<Test> {
    useJUnitPlatform()
}


gradlePlugin {
    plugins {
        register("library-config") {
            id = libs.plugins.hijrahDatetime.library.get().pluginId
            implementationClass = "LibraryConfigConventionPlugin"
        }
    }
}