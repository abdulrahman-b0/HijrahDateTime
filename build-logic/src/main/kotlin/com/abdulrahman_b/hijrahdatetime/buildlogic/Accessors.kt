package com.abdulrahman_b.hijrahdatetime.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

//@formatter:off //Disable formatting for the block below

fun DependencyHandlerScope.implementation(dependencyNotation: Any) = "implementation"(dependencyNotation)
fun DependencyHandlerScope.debugImplementation(dependencyNotation: Any) = "debugImplementation"(dependencyNotation)
fun DependencyHandlerScope.testImplementation(dependencyNotation: Any) = "testImplementation"(dependencyNotation)
fun DependencyHandlerScope.androidTestImplementation(dependencyNotation: Any) = "androidTestImplementation"(dependencyNotation)
fun DependencyHandlerScope.api(dependencyNotation: Any) = "api"(dependencyNotation)

internal fun Project.kotlin(action: KotlinJvmExtension.() -> Unit) = extensions.configure(KotlinJvmExtension::class, action)

internal fun Project.kotlinAndroid(action: KotlinAndroidProjectExtension.() -> Unit) = extensions.configure(KotlinAndroidProjectExtension::class, action)
internal fun Project.kotlinMultiplatform(action: KotlinMultiplatformExtension.() -> Unit) = extensions.configure(KotlinMultiplatformExtension::class, action)
internal val Project.kotlinMultiplatform get() = the<KotlinMultiplatformExtension>()

internal fun Project.mavenPublishing(action: MavenPublishBaseExtension.() -> Unit) = extensions.configure(MavenPublishBaseExtension::class, action)
internal val Project.mavenPublishing get() = the<MavenPublishBaseExtension>()

@JvmName("androidApplication")
internal fun Project.androidApplication(action: ApplicationExtension.() -> Unit) = extensions.configure(ApplicationExtension::class, action)

@JvmName("androidKotlinMultiplatformLibrary")
internal fun KotlinMultiplatformExtension.android(action: KotlinMultiplatformAndroidLibraryTarget.() -> Unit) = extensions.configure(KotlinMultiplatformAndroidLibraryTarget::class, action)
internal fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) = extensions.configure<ComposeCompilerGradlePluginExtension>(block)
