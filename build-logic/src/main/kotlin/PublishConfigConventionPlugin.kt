import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.the
import java.io.File
import java.util.Properties

class PublishConfigConventionPlugin : Plugin<Project> {

    private lateinit var globalProperties: Properties
    private lateinit var publishProperties: Properties
    private lateinit var hijrahDateTimePublishing: HijrahDateTimePublishingExtension

    override fun apply(target: Project) = with(target) {

        val libs = the<LibrariesForLibs>()
        plugins.apply(libs.plugins.vanniktechMavenPublish.get().pluginId)
        extensions.create<HijrahDateTimePublishingExtension>("hijrahDateTimePublishing")

        loadProperties()

        afterEvaluate {
            hijrahDateTimePublishing = the<HijrahDateTimePublishingExtension>()

            if (!hijrahDateTimePublishing.artifactId.isPresent) {
                logger.warn("hijrahDateTimePublishing.artifactId is not set, skipping publishing configuration")
                return@afterEvaluate
            }

            configurePublishing()

        }

    }

    private fun Project.loadProperties() {
        publishProperties = Properties().apply {
            load(File("publish.properties").reader())
        }
        globalProperties = Properties().apply {
            val userHome = this@loadProperties.gradle.gradleUserHomeDir
            load(File(userHome, "gradle.properties").reader())
        }
    }

    private fun Project.configurePublishing() {
        mavenPublishing {

            coordinates(
                groupId = rootProject.group.toString(),
                artifactId = hijrahDateTimePublishing.artifactId.get(),
                version = rootProject.version.toString()
            )

            publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = false)
            pom { configurePom(this) }

            signAllPublications()
        }

        publishing {
            repositories {
                maven("https://maven.abdulrahman-b.com/releases") {
                    name = "Reposilite"
                    credentials {
                        this.username = globalProperties.getProperty("reposilite.username")
                        this.password = globalProperties.getProperty("reposilite.password")
                    }
                }
            }
        }
    }

    private fun Project.configurePom(target: MavenPom) = with(target) {
        name = hijrahDateTimePublishing.artifactName.get()
        description = hijrahDateTimePublishing.artifactDescription.get()
        url = publishProperties.getProperty("project.url")
        licenses {
            license {
                name = publishProperties.getProperty("project.license.name")
                url = publishProperties.getProperty("project.license.url")
            }
        }

        developers {
            developer {
                id = globalProperties.getProperty("developer.id")
                name = globalProperties.getProperty("developer.name")
                email = globalProperties.getProperty("developer.email")
            }
        }

        scm {
            connection.set(publishProperties.getProperty("scm.connection"))
            developerConnection.set(publishProperties.getProperty("scm.developerConnection"))
            url.set(publishProperties.getProperty("scm.url"))
        }
    }

}


interface HijrahDateTimePublishingExtension {
    val artifactName: Property<String>
    val artifactDescription: Property<String>
    val artifactId: Property<String>
}
