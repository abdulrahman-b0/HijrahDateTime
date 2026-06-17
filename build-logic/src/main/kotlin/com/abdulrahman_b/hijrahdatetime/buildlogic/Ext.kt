package com.abdulrahman_b.hijrahdatetime.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.library(name: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(name).get()
}

fun VersionCatalog.plugin(name: String): Provider<PluginDependency> {
    return findPlugin(name).get()
}

internal fun PluginManager.applyAlias(notation: Provider<PluginDependency>) = apply(notation.get().pluginId)

internal infix fun ExtraPropertiesExtension.mark(key: String) = set(key, Unit)
internal infix fun ExtraPropertiesExtension.isMarked(key: String) = has(key)
internal infix fun ExtraPropertiesExtension.isNotMarked(key: String) = !has(key)