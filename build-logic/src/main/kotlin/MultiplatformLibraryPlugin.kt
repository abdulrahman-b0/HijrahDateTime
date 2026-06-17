@file:Suppress("UnstableApiUsage")

import com.abdulrahman_b.hijrahdatetime.buildlogic.ProjectConfig
import com.abdulrahman_b.hijrahdatetime.buildlogic.android
import com.abdulrahman_b.hijrahdatetime.buildlogic.applyAlias
import com.abdulrahman_b.hijrahdatetime.buildlogic.isMarked
import com.abdulrahman_b.hijrahdatetime.buildlogic.isNotMarked
import com.abdulrahman_b.hijrahdatetime.buildlogic.kotlinMultiplatform
import com.abdulrahman_b.hijrahdatetime.buildlogic.library
import com.abdulrahman_b.hijrahdatetime.buildlogic.libs
import com.abdulrahman_b.hijrahdatetime.buildlogic.mark
import com.abdulrahman_b.hijrahdatetime.buildlogic.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

class MultiplatformLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.multiplatformLibrary()
}

@Suppress("UnusedVariable")
@OptIn(ExperimentalKotlinGradlePluginApi::class)
private fun Project.multiplatformLibrary() {

    pluginManager.apply {
        applyAlias(libs.plugin("kotlin-multiplatform"))
        applyAlias(libs.plugin("kotlin-serialization"))
        applyAlias(libs.plugin("android-kotlin-multiplatform-library"))
    }

    kotlinMultiplatform {

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }

        android {
            minSdk = ProjectConfig.ANDROID_MIN_SDK
            compileSdk = ProjectConfig.ANDROID_COMPILE_SDK
            androidResources.enable = true
            withHostTest {
                isIncludeAndroidResources = true
            }
            withDeviceTest {
                instrumentationRunner = ProjectConfig.ANDROID_TEST_INSTRUMENTATION_RUNNER
            }
            optimization {
                consumerKeepRules.publish = true
                consumerKeepRules.file("consumer-rules.pro")
            }
            compilerOptions {
                jvmTarget.set(ProjectConfig.ANDROID_JVM_TARGET)
            }
            aarMetadata {
                minCompileSdk = ProjectConfig.ANDROID_MIN_SDK
            }
        }

        jvm {
            compilerOptions {
                jvmTarget.set(ProjectConfig.DESKTOP_JVM_TARGET)
            }
        }

        iosArm64()
        iosSimulatorArm64()
        macosArm64()

        sourceSets {

            this.commonMain.dependencies {
                implementation(libs.library("kotlin-datetime"))
                implementation(libs.library("kotlin-serialization-json"))
            }

            this.commonTest.dependencies {
                implementation(kotlin("test"))
                implementation(libs.library("kotest-assertions-core"))
            }


        }

    }

}

private const val DEFAULT_HIERARCHY_TEMPLATE_APPLIED = "default_hierarchy_template_applied"
private const val NON_ANDROID_SOURCE_SET_APPLIED = "non_android_source_set_applied"

@OptIn(ExperimentalKotlinGradlePluginApi::class)
val KotlinMultiplatformExtension.nonAndroidMain: KotlinSourceSet
    get() {
        if (extra isMarked NON_ANDROID_SOURCE_SET_APPLIED)
            return sourceSets.getByName("nonAndroidMain")

        applyDefaultHierarchyTemplate {
            common {
                group("nonAndroid") {
                    withJvm()
                    withIos()
                }
            }
        }

        extra mark DEFAULT_HIERARCHY_TEMPLATE_APPLIED
        extra mark NON_ANDROID_SOURCE_SET_APPLIED

        return sourceSets.getByName("nonAndroidMain")
    }

@OptIn(ExperimentalKotlinGradlePluginApi::class)
val KotlinMultiplatformExtension.nonAndroidTest: KotlinSourceSet
    get() {
        nonAndroidMain // ensure sourceset is initialized
        return sourceSets.getByName("nonAndroidTest")
    }

@OptIn(ExperimentalKotlinGradlePluginApi::class)
val KotlinMultiplatformExtension.mobileMain: KotlinSourceSet
    get() {
        val androidMain = sourceSets.androidMain.get()
        val iosMain = sourceSets.iosMain.get()

        val mobileMain by sourceSets.registering {
            dependsOn(sourceSets.commonMain.get())
            androidMain.dependsOn(this)
            iosMain.dependsOn(this)
        }
        if (extra isNotMarked DEFAULT_HIERARCHY_TEMPLATE_APPLIED)
            applyDefaultHierarchyTemplate()

        extra mark DEFAULT_HIERARCHY_TEMPLATE_APPLIED

        return mobileMain.get()
    }

@OptIn(ExperimentalKotlinGradlePluginApi::class)
val KotlinMultiplatformExtension.mobileTest: KotlinSourceSet
    get() {
        val iosTest = sourceSets.iosTest.get()
        val mobileTest by sourceSets.registering {
            dependsOn(sourceSets.commonTest.get())
            sourceSets.getByName("androidHostTest").dependsOn(this)
            iosTest.dependsOn(this)
        }
        return mobileTest.get()
    }
/**
 * Represents the [KotlinSourceSet] for shared code between JVM and Android platforms in a Kotlin Multiplatform project.
 */
@OptIn(ExperimentalKotlinGradlePluginApi::class)
val KotlinMultiplatformExtension.jvmCommonMain: KotlinSourceSet
    get() {
        val jvmCommonMain by sourceSets.registering {
            dependsOn(sourceSets.commonMain.get())
            sourceSets.androidMain.get().dependsOn(this)
        }

        if (extra isNotMarked DEFAULT_HIERARCHY_TEMPLATE_APPLIED)
            applyDefaultHierarchyTemplate()

        extra mark DEFAULT_HIERARCHY_TEMPLATE_APPLIED

        return jvmCommonMain.get()
    }



@OptIn(ExperimentalKotlinGradlePluginApi::class)
val KotlinMultiplatformExtension.jvmCommonTest: KotlinSourceSet
    get() {
        val jvmCommonTest by sourceSets.registering {
            dependsOn(sourceSets.commonTest.get())
            sourceSets.getByName("androidHostTest").dependsOn(this)
            sourceSets.getByName("androidDeviceTest").dependsOn(this)
        }
        return jvmCommonTest.get()
    }