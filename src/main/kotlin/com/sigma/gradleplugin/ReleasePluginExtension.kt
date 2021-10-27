package com.sigma.gradleplugin

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

abstract class ReleasePluginExtension(objectFactory: ObjectFactory, project: Project) {

    val deploymentTicket = objectFactory.property(String::class.java)

    val projectRepoPath = objectFactory.property(String::class.java)
        .convention("${project.rootDir.absolutePath}/.git")

    val cloudConfigRepoPath = objectFactory.property(String::class.java)
        .convention("${project.rootDir.absolutePath}/../config/.git")

    val bootstrapFilePath = objectFactory.property(String::class.java)
        .convention("${project.projectDir.absolutePath}/src/main/resources/bootstrap.yml")

    val gradlePropertiesFilePath = objectFactory.property(String::class.java)
        .convention("${project.projectDir.absolutePath}/gradle.properties")

    val tagName = objectFactory.property(String::class.java)
        .convention("${project.name}-${project.version}")

    val releaseBranchName = objectFactory.property(String::class.java)
        .convention("release-${project.version}")

    val applicationMainBranch = objectFactory.property(String::class.java)
        .convention("master")

    val cloudConfigMainBranch = objectFactory.property(String::class.java)
        .convention("master")

    val preReleaseCommitMessage = objectFactory.property(String::class.java)
        .convention("$deploymentTicket: [Release process] Pre release commit, version: '${project.version}'")

    val newVersionCommitMessage = objectFactory.property(String::class.java)
        .convention("$deploymentTicket: [Release process] Changed version number to '${project.version}'")
}