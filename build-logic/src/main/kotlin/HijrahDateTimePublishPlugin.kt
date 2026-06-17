import com.abdulrahman_b.hijrahdatetime.buildlogic.isMarked
import com.abdulrahman_b.hijrahdatetime.buildlogic.mark
import com.abdulrahman_b.hijrahdatetime.buildlogic.mavenPublishing
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.extra

class HijrahDateTimePublishPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {

        apply<MavenPublishPlugin>()

        extensions.create("hijrahDateTimePublishing", HijrahDateTimePublishExtension::class.java)

    }
}

interface HijrahDateTimePublishExtension {

    fun Project.mavenCoordinates(
        artifactId: String,
        artifactName: String,
    ) {

        if (extra isMarked PUBLISH_CONFIGURED)
            return

        mavenPublishing {

            coordinates(
                groupId = rootProject.group.toString(),
                artifactId = artifactId,
                version = rootProject.version.toString()
            )

            signAllPublications()

            pom {
                name.set("HijrahDateTime Compose Pickers")
                description.set(rootProject.description)
                url.set("https://github.com/abdulrahman-b0/HijrahDateTime")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://mit-license.org/")
                    }
                }

                developers {
                    developer {
                        id.set("abdulrahman-b")
                        name.set("Abdulrahman Bahamel")
                        email.set("abdulrahman-b0@hotmail.com")
                    }
                }

            }
        }

        extra mark PUBLISH_CONFIGURED

    }
}

private const val PUBLISH_CONFIGURED = "PUBLISH_CONFIGURED"