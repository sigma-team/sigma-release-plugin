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
        rootProject.pluginManager.withPlugin("java-base") {
            val buildTask = rootProject.tasks.getByName("build")
            buildTask.dependsOn(preBuildReleaseTask)
            buildTask.finalizedBy(postBuildReleaseTask)
        }
    }

    private fun createReleaseExtension(rootProject: Project) =
        rootProject.extensions.create<ReleasePluginExtension>("sigmaReleasePlugin", rootProject)

    private fun createPreBuildReleaseTask(
        rootProject: Project,
        extension: ReleasePluginExtension
    ) = rootProject.tasks.register<PreBuildReleaseTask>("preBuildRelease") {
        group = tasksGroup
        description = "TODO"

        deploymentTicket.set(extension.deploymentTicket)
        projectRepoPath.set(extension.projectRepoPath)
        releaseBranchName.set(extension.releaseBranchName)
        bootstrapFilePath.set(extension.bootstrapFilePath)
        tagName.set(extension.tagName)
        preReleaseCommitMessage.set(extension.preReleaseCommitMessage)
    }

    private fun createPostBuildReleaseTask(
        rootProject: Project,
        extension: ReleasePluginExtension
    ) = rootProject.tasks.register<PostBuildReleaseTask>("postBuildRelease") {
        group = tasksGroup
        description = "TODO"

        deploymentTicket.set(extension.deploymentTicket)
        projectRepoPath.set(extension.projectRepoPath)
        cloudConfigRepoPath.set(extension.cloudConfigRepoPath)
        applicationMainBranch.set(extension.applicationMainBranch)
        gradlePropertiesFilePath.set(extension.gradlePropertiesFilePath)
        cloudConfigMainBranch.set(extension.cloudConfigMainBranch)
        tagName.set(extension.tagName)
        preReleaseCommitMessage.set(extension.preReleaseCommitMessage)
        newVersionCommitMessage.set(extension.newVersionCommitMessage)
    }
}