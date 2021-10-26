package com.sigma.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class SigmaReleasePlugin : Plugin<Project> {

    private val tasksGroup = "sigma release"

    override fun apply(rootProject: Project) {

        val extension = createReleaseExtension(rootProject)
        val preBuildReleaseTask = createPreBuildReleaseTask(rootProject, extension)
        val postBuildReleaseTask = createPostBuildReleaseTask(rootProject, extension)
        print("====tasks" + rootProject.tasks.asMap)
        rootProject.pluginManager.withPlugin("java-base") {
            val buildTask = rootProject.tasks.getByName("build")
            buildTask.dependsOn(preBuildReleaseTask)
            buildTask.finalizedBy(postBuildReleaseTask)
        }
    }

    private fun createReleaseExtension(rootProject: Project) =
        rootProject.extensions.create<ReleasePluginExtension>("sigmaReleasePlugin", rootProject.objects)

    private fun createPreBuildReleaseTask(
        rootProject: Project,
        extension: ReleasePluginExtension
    ) = rootProject.tasks.register<PreBuildReleaseTask>("preBuildRelease") {
        group = tasksGroup
        description = "TODO"

        ticketNumber.set(extension.ticketNumber)
        version.set(extension.version)
        preReleaseCommitMessage.set(extension.preReleaseCommitMessage)
        newVersionCommitMessage.set(extension.newVersionCommitMessage)
    }

    private fun createPostBuildReleaseTask(
        rootProject: Project,
        extension: ReleasePluginExtension
    ) = rootProject.tasks.register<PostBuildReleaseTask>("postBuildRelease") {
        group = tasksGroup
        description = "TODO"

        ticketNumber.set(extension.ticketNumber)
        version.set(extension.version)
        preReleaseCommitMessage.set(extension.preReleaseCommitMessage)
        newVersionCommitMessage.set(extension.newVersionCommitMessage)
    }
}