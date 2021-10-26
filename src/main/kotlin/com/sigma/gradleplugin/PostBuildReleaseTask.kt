package com.sigma.gradleplugin

import com.sigma.domain.postBuild
import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class PostBuildReleaseTask @Inject constructor(
    objectFactory: ObjectFactory
) : DefaultTask() {

    @get:Input
    val projectRepoPath = objectFactory.property(String::class.java)

    @get:Input
    val cloudConfigRepoPath = objectFactory.property(String::class.java)

    @get:Input
    val applicationMainBranch = objectFactory.property(String::class.java)

    @get:Input
    val cloudConfigMainBranch = objectFactory.property(String::class.java)

    @get:Input
    val tagName = objectFactory.property(String::class.java)

    @get:Input
    val preReleaseCommitMessage = objectFactory.property(String::class.java)

    @get:Input
    val newVersionCommitMessage = objectFactory.property(String::class.java)

    @TaskAction
    fun invoke() {
        logger.quiet("run sigma release plugin `postBuild` step")

        postBuild(
            projectRepoPath = projectRepoPath.get(),
            cloudConfigRepoPath = cloudConfigRepoPath.get(),
            applicationMainBranch = applicationMainBranch.get(),
            cloudConfigMainBranch = cloudConfigMainBranch.get(),
            tagName = tagName.get(),
            preReleaseCommitMessage = preReleaseCommitMessage.get(),
            newVersionCommitMessage = newVersionCommitMessage.get()
        )
    }
}