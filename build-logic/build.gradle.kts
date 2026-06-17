plugins {
    `kotlin-dsl`
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    plugin(libs.plugins.vanniktechMavenPublish)
    plugin(libs.plugins.android.kotlin.multiplatform.library)
    plugin(libs.plugins.composeMultiplatform)
    plugin(libs.plugins.kotlin.compose)
    plugin(libs.plugins.kotlin.serialization)
}

fun DependencyHandlerScope.plugin(dependencyNotation: Provider<PluginDependency>) {
    val pluginDep = dependencyNotation.get()
    val pluginId = pluginDep.pluginId
    val pluginVersion = pluginDep.version
    val markerArtifact = "$pluginId:$pluginId.gradle.plugin:$pluginVersion"

    implementation(markerArtifact)
}

gradlePlugin {
    plugins {
        register("MultiplatformLibraryPlugin") {
            id = libs.plugins.hijrahdatetime.multiplatform.library.get().pluginId
            implementationClass = "MultiplatformLibraryPlugin"
        }
        register("MultiplatformComposeLibraryPlugin") {
            id = libs.plugins.hijrahdatetime.multiplatform.compose.library.get().pluginId
            implementationClass = "MultiplatformComposeLibraryPlugin"
        }
        register("HijrahDateTimePublishPlugin") {
            id = libs.plugins.hijrahdatetime.publish.get().pluginId
            implementationClass = "HijrahDateTimePublishPlugin"
        }
    }
}