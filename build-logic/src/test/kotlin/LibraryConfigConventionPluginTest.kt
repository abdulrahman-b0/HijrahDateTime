import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

//class LibraryConfigConventionPluginTest {
//
//
//    companion object {
//
//        lateinit var project: Project
//
//
//        @BeforeAll
//        @JvmStatic
//        fun setup() {
//            project = ProjectBuilder.builder().build()
//            project.pluginManager.apply(LibraryConfigConventionPlugin::class.java)
//        }
//    }
//
//    @Test
//    @DisplayName("Plugins are applied")
//    fun applyPlugins() {
//        assertTrue(project.plugins.hasPlugin("java-library"))
//        assertTrue(project.plugins.hasPlugin("org.jetbrains.kotlin.jvm"))
//    }
//
//    @Test
//    @DisplayName("Group and version are set properly")
//    fun groupAndVersion() {
//        assertEquals("com.abdulrahman_b", project.group)
//        assertEquals("1.0.0-beta.3", project.version)
//    }
//
//    @Test
//    @DisplayName("Java compatibility is set")
//    fun javaCompatibility() {
//        val javaExtension = project.the<JavaPluginExtension>()
//        assertEquals(JavaVersion.VERSION_17, javaExtension.sourceCompatibility)
//        assertEquals(JavaVersion.VERSION_17, javaExtension.targetCompatibility)
//    }
//
//    @Test
//    @DisplayName("Kotlin jvm target is set")
//    fun kotlinJvmTarget() {
//        val kotlinExtension = project.the<KotlinJvmProjectExtension>()
//        assertEquals(JvmTarget.JVM_17, kotlinExtension.compilerOptions.jvmTarget.get())
//    }
//
//    @Test
//    @DisplayName("Dependencies are added")
//    fun dependencies() {
//        val testImplDependencies =
//            project.configurations.getByName("testImplementation").dependencies
//        assertTrue(testImplDependencies.any { it.group == "org.junit.jupiter" && it.name == "junit-jupiter" })
//        assertTrue(testImplDependencies.any { it.group == "org.junit.jupiter" && it.name == "junit-jupiter-api" })
//    }
//
//}