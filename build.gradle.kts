import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow").version("7.1.0")
    `kotlin-dsl`
    `maven-publish`
}

group "org.amobee"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("sigmaReleasePlugin") {
            description = "TODO"
            displayName = "Sigma release plugin"
            id = "com.sigma.release"
            implementationClass = "com.sigma.gradleplugin.SigmaReleasePlugin"
        }
    }
}

/**
 * Special configuration to be included in resulting shadowed jar, but not added to the generated pom and gradle
 * metadata files.
 */
val shadowImplementation by configurations.creating
configurations["compileOnly"].extendsFrom(shadowImplementation)
configurations["testImplementation"].extendsFrom(shadowImplementation)

dependencies {
    implementation("commons-configuration:commons-configuration:1.10")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    shadowImplementation("org.eclipse.jgit:org.eclipse.jgit:5.13.0.202109080827-r")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {
    shadowJar<ShadowJar> {
        configurations = listOf(shadowImplementation)
        archiveName = "sigma-team.sigma-release-plugin.jar"
    }
    jar {
        archiveBaseName.set("sigma-team.sigma-release-plugin")

        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }

    test {
        useJUnitPlatform()
    }
}