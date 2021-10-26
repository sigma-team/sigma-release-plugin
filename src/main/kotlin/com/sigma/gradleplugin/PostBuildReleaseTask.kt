package com.sigma.gradleplugin

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class PostBuildReleaseTask @Inject constructor(
    objectFactory: ObjectFactory
) : DefaultTask() {

    @get:Input
    val ticketNumber = objectFactory.property(String::class.java)

    @get:Input
    val version = objectFactory.property(String::class.java)

    @get:Input
    val preReleaseCommitMessage = objectFactory.property(String::class.java)

    @get:Input
    val newVersionCommitMessage = objectFactory.property(String::class.java)

    @TaskAction
    fun invoke() {
        logger.quiet("run sigma release plugin `postBuild` step")
    }

}