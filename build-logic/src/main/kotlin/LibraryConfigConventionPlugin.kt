import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


class LibraryConfigConventionPlugin: Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        val libs = the<LibrariesForLibs>()

        plugins.apply("java-library")
        plugins.apply(libs.plugins.kotlin.jvm.get().pluginId)
        plugins.apply(libs.plugins.kotlin.kover.get().pluginId)

        group = rootProject.group
        version = rootProject.version

        kotlin {
            compilerOptions {
                jvmTarget = JvmTarget.JVM_17
            }
        }

        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        dependencies {
            testImplementation(libs.junit.jupiter)
            testImplementation(libs.junit.jupiter.api)
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }

    }

}