import com.abdulrahman_b.hijrahdatetime.buildlogic.applyAlias
import com.abdulrahman_b.hijrahdatetime.buildlogic.kotlinMultiplatform
import com.abdulrahman_b.hijrahdatetime.buildlogic.library
import com.abdulrahman_b.hijrahdatetime.buildlogic.libs
import com.abdulrahman_b.hijrahdatetime.buildlogic.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke

class MultiplatformComposeLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.multiplatformLibrary()
}

private fun Project.multiplatformLibrary() {

    with(pluginManager) {
        apply<MultiplatformLibraryPlugin>()
        applyAlias(libs.plugin("kotlin-compose"))
        applyAlias(libs.plugin("composeMultiplatform"))
        applyAlias(libs.plugin("kotlin-serialization"))
    }

    kotlinMultiplatform {

        sourceSets {


            commonMain.dependencies {
                implementation(libs.library("composeMultiplatform-runtime"))
                implementation(libs.library("composeMultiplatform-foundation"))
                implementation(libs.library("composeMultiplatform-ui"))
                implementation(libs.library("composeMultiplatform-ui-graphics"))
                implementation(libs.library("composeMultiplatform-ui-tooling-preview"))
                implementation(libs.library("composeMultiplatform-components-resources"))
                implementation(libs.library("composeMultiplatform-material3"))
                implementation(libs.library("composeMultiplatform-material-iconsCore"))
            }
        }
    }


    dependencies {
        "androidRuntimeClasspath"(libs.findLibrary("composeMultiplatform-ui-tooling").get())
    }
}
