import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension


internal fun Project.java(block: JavaPluginExtension.() -> Unit) {
    extensions.configure<JavaPluginExtension>(block)
}

internal fun Project.kotlin(block: KotlinJvmProjectExtension.() -> Unit) {
    extensions.configure<KotlinJvmProjectExtension>(block)
}

internal fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    "implementation"(dependencyNotation)
}

internal fun DependencyHandlerScope.testImplementation(dependencyNotation: Any) {
    "testImplementation"(dependencyNotation)
}
