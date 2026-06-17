import com.abdulrahman_b.hijrahdatetime.buildlogic.ProjectConfig
import com.abdulrahman_b.hijrahdatetime.buildlogic.androidApplication
import com.abdulrahman_b.hijrahdatetime.buildlogic.androidTestImplementation
import com.abdulrahman_b.hijrahdatetime.buildlogic.applyAlias
import com.abdulrahman_b.hijrahdatetime.buildlogic.debugImplementation
import com.abdulrahman_b.hijrahdatetime.buildlogic.implementation
import com.abdulrahman_b.hijrahdatetime.buildlogic.kotlinAndroid
import com.abdulrahman_b.hijrahdatetime.buildlogic.library
import com.abdulrahman_b.hijrahdatetime.buildlogic.libs
import com.abdulrahman_b.hijrahdatetime.buildlogic.plugin
import com.abdulrahman_b.hijrahdatetime.buildlogic.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = target.androidApplication()
}

private fun Project.androidApplication() {


    pluginManager.apply {
        applyAlias(libs.plugin("android-application"))
        applyAlias(libs.plugin("kotlin-serialization"))
        applyAlias(libs.plugin("kotlin-compose"))
    }

    androidApplication {

        defaultConfig {
            targetSdk = ProjectConfig.ANDROID_TARGET_SDK
            minSdk = ProjectConfig.ANDROID_MIN_SDK
            testInstrumentationRunner = ProjectConfig.ANDROID_TEST_INSTRUMENTATION_RUNNER
        }
        compileSdk = ProjectConfig.ANDROID_COMPILE_SDK

        compileOptions.apply {
            sourceCompatibility = ProjectConfig.ANDROID_JAVA_VERSION
            targetCompatibility = ProjectConfig.ANDROID_JAVA_VERSION
            isCoreLibraryDesugaringEnabled = false
        }

        kotlinAndroid {
            compilerOptions {
                jvmTarget.set(ProjectConfig.ANDROID_JVM_TARGET)
            }
        }

        testOptions.apply {
            unitTests {
                isIncludeAndroidResources = true
            }
        }



        buildFeatures.apply {
            buildConfig = true
            compose = true
        }

        buildTypes {
            debug {
                isMinifyEnabled = false
                isShrinkResources = false
                manifestPlaceholders["pushServiceExported"] = true
            }
            release {
                isMinifyEnabled = true
                isShrinkResources = true
                signingConfig = signingConfigs.getByName("release")
                manifestPlaceholders["pushServiceExported"] = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        productFlavors {
            flavorDimensions.add("mobile services")
            create("gms") {  //Google Mobile Services
                dimension = "mobile services"
                manifestPlaceholders["gmsPushEnabled"] = true
                manifestPlaceholders["hmsPushEnabled"] = false
            }
            create("hms") { //Huawei Mobile Services
                dimension = "mobile services"
                manifestPlaceholders["gmsPushEnabled"] = false
                manifestPlaceholders["hmsPushEnabled"] = true
            }
        }


    }


    dependencies {
        implementation(project(":core"))
        implementation(project(":compose-pickers"))
        implementation(libs.library("androidx-appCompat"))
        implementation(libs.library("androidx-lifecycle-runtime-ktx"))
        implementation(libs.library("androidx-activity-compose"))

        implementation(libs.library("androidx-core-ktx"))
        implementation(libs.library("androidx-appCompat"))
        implementation(libs.library("kotlin-coroutines-android"))
        implementation(libs.library("kotlin-datetime"))
        implementation(libs.library("kotlin-serialization-json"))
        testImplementation(libs.library("kotlin-coroutines-test"))
        testImplementation(kotlin("test-junit5"))
        testImplementation(libs.library("kotest-assertions-core"))
        androidTestImplementation(kotlin("test"))
        androidTestImplementation(libs.library("junit"))
        androidTestImplementation(libs.library("androidx-junit"))
        androidTestImplementation(libs.library("kotest-assertions-core"))
        androidTestImplementation(libs.library("kotlin-coroutines-test"))

        implementation(this.platform(libs.library("androidx-compose-bom")))
        implementation(libs.library("androidx-ui"))
        implementation(libs.library("androidx-ui-graphics"))
        implementation(libs.library("androidx-ui-tooling-preview"))
        implementation(libs.library("androidx-material3"))
        androidTestImplementation(this.platform(libs.library("androidx-compose-bom")))
        androidTestImplementation(libs.library("androidx-ui-test-junit4"))
        debugImplementation(libs.library("androidx-ui-tooling"))
        debugImplementation(libs.library("androidx-ui-test-manifest"))
    }

}