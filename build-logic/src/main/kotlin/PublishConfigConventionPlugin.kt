import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.the
import java.io.File
import java.util.Properties

class PublishConfigConventionPlugin : Plugin<Project> {

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

    private fun loadProperties() {
        publishProperties = Properties().apply {
            load(File("publish.properties").reader())
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

        tasks.register("publishCoordinatesCheck") {
            doLast {
                logger.trace("Running publish coordinates check")
                check(rootProject.group == "com.abdulrahman-b") {
                    "[INVALID] The root project group must be 'com.abdulrahman-b'"
                }
                logger.info("[VALID] Root project group: ${rootProject.group}")


                check(rootProject.name == "HijrahDateTime") {
                    "[INVALID] The root project name must be 'HijrahDateTime'"
                }
                logger.info("[VALID] Root project name: ${rootProject.name}")

                check(hijrahDateTimePublishing.artifactId.get().startsWith(rootProject.name)) {
                    "[INVALID] The artifactId must start with the root project name"
                }
                logger.info("[VALID] ArtifactId: ${hijrahDateTimePublishing.artifactId.get()}")
            }
        }

        tasks.getByName("publishToMavenLocal").dependsOn("publishCoordinatesCheck")
        tasks.getByName("publish").dependsOn("publishCoordinatesCheck")
    }

    private fun Project.configurePom(target: MavenPom) = with(target) {
        name = rootProject.name
        description = rootProject.description
        url = publishProperties.getProperty("project.url")
        licenses {
            license {
                name = publishProperties.getProperty("project.license.name")
                url = publishProperties.getProperty("project.license.url")
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
            connection.set(publishProperties.getProperty("scm.connection"))
            developerConnection.set(publishProperties.getProperty("scm.developerConnection"))
            url.set(publishProperties.getProperty("scm.url"))
        }
    }

}


interface HijrahDateTimePublishingExtension {
    val artifactId: Property<String>
}
