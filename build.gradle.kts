@file:Suppress("MemberVisibilityCanBePrivate", "unused")
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jreleaser.model.Active
import org.jreleaser.model.Signing
import org.jreleaser.util.Algorithm
import java.lang.System.load
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.jreleaser)
    `maven-publish`
}


group = "com.abdulrahman-b"
version = "1.0.0-beta.2"

java {
    withJavadocJar()
    withSourcesJar()
}

val publishProperties = Properties().apply {
    load(file("publish.properties").reader())
}


publishing {

    publications {
        create<MavenPublication>(project.name) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])

            pom {
                name = publishProperties.getProperty("project.name")
                description = publishProperties.getProperty("project.description")
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
    }

    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }

}

val gradleProject = project

jreleaser {
    project {
        name = gradleProject.name
        version = gradleProject.version.toString()
        description = publishProperties.getProperty("project.description")
        license = publishProperties.getProperty("project.license.name")
        authors = listOf(publishProperties.getProperty("developer.name"))
        links {
            homepage = publishProperties.getProperty("project.url")
        }
    }

    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
        mode.set(Signing.Mode.FILE)
        publicKey.set(publishProperties.getProperty("signing.publicKey"))
        secretKey.set(publishProperties.getProperty("signing.privateKey"))
        passphrase.set(publishProperties.getProperty("signing.passphrase"))
    }

    checksum {
        algorithms.addAll(Algorithm.MD5, Algorithm.SHA_1, Algorithm.SHA_256, Algorithm.SHA_512)
    }

    release {
        github {
            enabled.set(true)
            host.set(publishProperties.getProperty("github.host"))
            repoOwner.set(publishProperties.getProperty("github.repository.owner"))
            name.set(publishProperties.getProperty("github.repository.name"))
            token.set(publishProperties.getProperty("github.token"))
            tagName.set(project.version.toString())
        }
    }

    deploy {
        maven {
            mavenCentral {
                register("sonatype") {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    username.set(publishProperties.getProperty("sonatype.username"))
                    password.set(publishProperties.getProperty("sonatype.password"))
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().toString())
                }
            }
        }
    }
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.serialization.json)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}