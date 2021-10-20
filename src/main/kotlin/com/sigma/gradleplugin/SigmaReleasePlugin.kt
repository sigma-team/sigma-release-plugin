package com.sigma.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class SigmaReleasePlugin : Plugin<Project> {

    override fun apply(rootProject: Project) {

        val extension = rootProject.extensions.create<ReleasePluginExtension>("sigmaReleasePlugin", rootProject.objects)

        rootProject.tasks.register<ReleaseTask>("release") {
            group = "deploy"
            description = "TODO"

            ticketNumber.set(extension.ticketNumber)
            version.set(extension.version)
            preReleaseCommitMessage.set(extension.preReleaseCommitMessage)
            newVersionCommitMessage.set(extension.newVersionCommitMessage)
        }
    }
}