package com.sigma.gradleplugin

import com.sigma.domain.preBuild
import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class PreBuildReleaseTask @Inject constructor(
    objectFactory: ObjectFactory
) : DefaultTask() {

    @get:Input
    val projectRepoPath = objectFactory.property(String::class.java)

    @get:Input
    val releaseBranchName = objectFactory.property(String::class.java)

    @get:Input
    val bootstrapFilePath = objectFactory.property(String::class.java)

    @get:Input
    val tagName = objectFactory.property(String::class.java)

    @get:Input
    val preReleaseCommitMessage = objectFactory.property(String::class.java)

    @TaskAction
    fun invoke() {
        logger.quiet("run sigma release plugin `preBuild` step")

        preBuild(
            projectRepoPath = projectRepoPath.get(),
            releaseBranchName = releaseBranchName.get(),
            bootstrapFile = bootstrapFilePath.get(),
            tagName = tagName.get(),
            preReleaseCommitMessage = preReleaseCommitMessage.get()
        )
    }
}