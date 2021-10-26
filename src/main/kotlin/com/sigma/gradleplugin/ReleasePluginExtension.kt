package com.sigma.gradleplugin

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

abstract class ReleasePluginExtension(objectFactory: ObjectFactory, rootProject: Project) {

    val deploymentTicket = objectFactory.property(String::class.java)

    val bootstrapFilePath = objectFactory.property(String::class.java)
        .convention("${rootProject.path}/src/main/resources")

    val tagName = objectFactory.property(String::class.java)
        .convention("${rootProject.name}-${rootProject.version}")

    val releaseBranchName = objectFactory.property(String::class.java)
        .convention("release-${rootProject.version}")

    val applicationMainBranch = objectFactory.property(String::class.java)
        .convention("master")

    val preReleaseCommitMessage = objectFactory.property(String::class.java)
        .convention("$deploymentTicket: [Release process] Pre release commit, version: '${rootProject.version}'")

    val newVersionCommitMessage = objectFactory.property(String::class.java)
        .convention("$deploymentTicket: [Release process] Changed version number to '${rootProject.version}'")
}