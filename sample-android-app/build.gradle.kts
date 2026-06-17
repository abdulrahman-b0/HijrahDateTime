@file:Suppress("UnstableApiUsage")


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {

    namespace = "com.abdulrahman_b.hijrahdatetime.sample"
    defaultConfig {
        targetSdk = 37
        minSdk = 26
//        testInstrumentationRunner = ProjectConfig.ANDROID_TEST_INSTRUMENTATION_RUNNER
    }
    compileSdk = 37

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = false
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
        }
    }

}


dependencies {
    implementation(projects.core)
    implementation(projects.composePickers)
    implementation(projects.sampleShared)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.serialization.json)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(kotlin("test.junit5"))
    testImplementation(libs.kotest.assertions.core)
    androidTestImplementation(kotlin("test"))
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.kotest.assertions.core)
    androidTestImplementation(libs.kotlin.coroutines.test)

    implementation(this.platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(this.platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}