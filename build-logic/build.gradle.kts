import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

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
    plugin(libs.plugins.kotlin.jvm)
    plugin(libs.plugins.kotlin.kover)
    plugin(libs.plugins.vanniktechMavenPublish)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

fun DependencyHandlerScope.plugin(dependencyNotation: Provider<PluginDependency>) {
    val pluginId = dependencyNotation.get().pluginId
    val version = dependencyNotation.get().version
    val markerArtifact = "$pluginId:$pluginId.gradle.plugin:$version"
    "implementation"(markerArtifact)
}

fun DependencyHandlerScope.plugin(id: String, version: String) {
    val markerArtifact = "$id:$id.gradle.plugin:$version"
    "implementation"(markerArtifact)
}