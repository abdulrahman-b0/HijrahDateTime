import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.vanniktechMavenPublish)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-XcontextParameters")
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        macosArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "HijrahDateTime"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.datetime)
            implementation(libs.kotlin.serialization.json)
        }

        jvmMain

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotest.assertions.core)
        }
    }
}

mavenPublishing {

    coordinates(
        groupId = rootProject.group.toString(),
        artifactId = "hijrahdatetime",
        version = rootProject.version.toString()
    )

    pom {

        val publishProperties = Properties().apply {
            val file = File("publish.properties")
            if (!file.exists()) return@apply
            load(file.reader())
        }

        name = "HijrahDateTime"
        description = rootProject.description
        url = "https://github.com/abdulrahman-b0/HijrahDateTime"

        licenses {
            license {
                name = "MIT License"
                url = "https://mit-license.org/"
            }
        }

        developers {
            developer {
                id = publishProperties.getProperty("developer.id")
                name = publishProperties.getProperty("developer.name")
                email = publishProperties.getProperty("developer.email")
            }
        }

        scm {
            connection.set("scm:git:https://github.com/abdulrahman-b0/HijrahDateTime.git")
            developerConnection.set("scm:git:git@github.com:abdulrahman-b0/HijrahDateTime.git")
            url.set("https://github.com/abdulrahman-b0/HijrahDateTime")
        }
    }

    signAllPublications()


}