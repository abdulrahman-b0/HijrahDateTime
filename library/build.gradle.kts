import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.vanniktechMavenPublish)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-XcontextParameters")
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }

    android {
        namespace = "com.abdulrahman_b.hijrahdatetime"
        compileSdk = 37
        minSdk = 26

        aarMetadata {
            minCompileSdk = 26
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        macosArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "HijrahDateTime"
            isStatic = true
        }
    }

    sourceSets {

        applyDefaultHierarchyTemplate()

        commonMain.dependencies {
            implementation(libs.kotlin.datetime)
            implementation(libs.kotlin.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotest.assertions.core)
        }


        val jvmCommonMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.kotlin.datetime.jvm)
            }
        }

        jvmMain.get().dependsOn(jvmCommonMain)
        androidMain.get().dependsOn(jvmCommonMain)


    }
}

mavenPublishing {

    coordinates(
        groupId = rootProject.group.toString(),
        artifactId = "hijrahdatetime",
        version = rootProject.version.toString()
    )

    pom {

        val publishProperties = Properties().apply {
            val file = File("publish.properties")
            if (!file.exists()) return@apply
            load(file.reader())
        }

        name = "HijrahDateTime"
        description = rootProject.description
        url = "https://github.com/abdulrahman-b0/HijrahDateTime"

        licenses {
            license {
                name = "MIT License"
                url = "https://mit-license.org/"
            }
        }

        developers {
            developer {
                id = publishProperties.getProperty("developer.id")
                name = publishProperties.getProperty("developer.name")
                email = publishProperties.getProperty("developer.email")
            }
        }

        scm {
            connection.set("scm:git:https://github.com/abdulrahman-b0/HijrahDateTime.git")
            developerConnection.set("scm:git:git@github.com:abdulrahman-b0/HijrahDateTime.git")
            url.set("https://github.com/abdulrahman-b0/HijrahDateTime")
        }
    }

    signAllPublications()


}


// Define the code generation task
val generateUmmAlQuraData by tasks.registering {
    group = "codegen"
    description = "Parses the raw Umm al-Qura properties file into a bit-packed Kotlin ShortArray."

    val classname = "UmmAlQuraData"
    // Tell Gradle to watch the input file for changes
    val inputFile =
        rootProject.layout.projectDirectory.file("calendar-data/hijrah-config-islamic-umalqura.properties").asFile
    val outputDir = file("${projectDir}/src/commonMain/kotlin/com/abdulrahman_b/hijrahdatetime")
    val outputFile = file("${outputDir}/internal/$classname.kt")

    inputs.file(inputFile)
    outputs.file(outputFile)

    doLast {
        if (!inputFile.exists()) {
            throw GradleException("Source properties file not found at: ${inputFile.absolutePath}")
        }

        val masksList = mutableListOf<String>()
        val daysOfYearList = mutableListOf<String>()
        var minYear = 9999
        var maxYear = 0

        // Parse lines and pack months into bits
        inputFile.readLines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isNotEmpty() && trimmed.first().isDigit()) {
                val parts = trimmed.split("=")
                val year = parts[0].trim().toInt()
                val months = parts[1].trim().split("\\s+".toRegex()).map { it.toInt() }

                // Track the calendar range boundary constants
                if (year < minYear) minYear = year
                if (year > maxYear) maxYear = year

                // Bit-pack 12 months (30 days -> 1, 29 days -> 0)
                var mask = 0
                months.forEachIndexed { index, days ->
                    if (days == 30) {
                        mask = mask or (1 shl (11 - index))
                    }
                }
                daysOfYearList.add("        ${months.sum()}, //Year $year AH")
                val maskBinary =
                    "0b" + mask.toString(2).padStart(12, '0').chunked(4).joinToString("_")
                masksList.add("        $maskBinary, // Year $year AH")
            }
        }

        // Generate the clean Kotlin source file code string
        val codeText = buildString {
            appendLine("// Automatically generated by Gradle task - DO NOT MODIFY MANUALLY")
            appendLine("package com.abdulrahman_b.hijrahdatetime.internal")
            appendLine()
            appendLine(
                """
                        /**
                        * Umm al-Qura calendar configuration data.
                        * * Each binary literal represents a single Hijri year where:
                        * - A bit at index `n` (from 0 to 11) corresponds to a month.
                        * - `1` (binary 0b1) = 30-day month.
                        * - `0` (binary 0b0) = 29-day month.
                        * * Bit positions are counted from left-to-right (Muharram to Dhu al-Hijjah).
                        * Example: `0b1010_1010_1010` = 30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29
                        */
                    """.trimIndent()
            )
            appendLine("internal object $classname {")
            appendLine("    const val BASE_HIJRI_YEAR = $minYear")
            appendLine("    const val MAX_HIJRI_YEAR = $maxYear")
            appendLine()
            appendLine("    val MONTH_MASKS = shortArrayOf(")
            masksList.forEach { appendLine(it) }
            appendLine("    )")
            appendLine()
            appendLine("    val DAYS_OF_YEARS = shortArrayOf(")
            daysOfYearList.forEach { appendLine(it) }
            appendLine("    )")
            appendLine("}")
        }

        // Ensure directories exist and write out the file
        outputDir.mkdirs()
        outputFile.writeText(codeText)
        logger.lifecycle("Successfully generated packed calendar data for years $minYear-$maxYear!")
    }
}

// Automatically trigger code generation whenever the Kotlin code compiles
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn(generateUmmAlQuraData)
}