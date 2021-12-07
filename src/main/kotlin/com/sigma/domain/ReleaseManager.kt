package com.sigma.domain

import org.eclipse.jgit.api.Git
import java.nio.file.Paths

const val DEPLOYMENT_TICKET_PLACEHOLDER = "{deploymentTicket}"
const val CURRENT_VERSION_PLACEHOLDER = "{currentVersion}"
const val NEW_VERSION_PLACEHOLDER = "{newVersion}"

fun preBuild(
    deploymentTicket: String,
    projectRepoPath: String,
    gradlePropertiesFilePath: String,
    releaseBranchName: String,
    bootstrapFile: String,
    tagName: String,
    preReleaseCommitMessage: String
) {
    val releaseBranchNameWithoutSuffix = releaseBranchName.removeSuffix("-SNAPSHOT")
    val git = createGit(projectRepoPath)
    val currentVersion = getCurrentVersion(gradlePropertiesFilePath).removeSuffix("-SNAPSHOT")
    checkout(git, releaseBranchNameWithoutSuffix, createNewBranch = true)
    addSpringCloudConfigLabel(bootstrapFile = bootstrapFile, tagName.replace(CURRENT_VERSION_PLACEHOLDER, currentVersion))
    add(git, Paths.get(projectRepoPath).toRealPath().relativize(Paths.get(bootstrapFile).toRealPath()).toString())
    commit(
        git,
        preReleaseCommitMessage
            .replace(DEPLOYMENT_TICKET_PLACEHOLDER, deploymentTicket)
            .replace(CURRENT_VERSION_PLACEHOLDER, currentVersion)
    )
    push(git, releaseBranchNameWithoutSuffix)
}

fun postBuild(
    deploymentTicket: String,
    projectRepoPath: String,
    cloudConfigRepoPath: String,
    gradlePropertiesFilePath: String,
    applicationMainBranch: String,
    cloudConfigMainBranch: String,
    tagName: String,
    preReleaseCommitMessage: String,
    newVersionCommitMessage: String
) {
    val projectGit = createGit(projectRepoPath)
    val cloudConfigGit = createGit(cloudConfigRepoPath)

    checkout(projectGit, applicationMainBranch)

    val currentVersion = getCurrentVersion(gradlePropertiesFilePath).removeSuffix("-SNAPSHOT")
    val newVersion = incrementAppVersion(gradlePropertiesFilePath)

    createTagInCloudConfigRepo(
        git = cloudConfigGit,
        cloudConfigMainBranch = cloudConfigMainBranch,
        tagName = tagName.replace(CURRENT_VERSION_PLACEHOLDER, currentVersion),
        preReleaseCommitMessage = preReleaseCommitMessage
            .replace(DEPLOYMENT_TICKET_PLACEHOLDER, deploymentTicket)
            .replace(CURRENT_VERSION_PLACEHOLDER, currentVersion)
    )
    pushNewProjectVersion(
        git = projectGit,
        projectRepoPath = projectRepoPath,
        gradlePropertiesFilePath = gradlePropertiesFilePath,
        applicationMainBranch = applicationMainBranch,
        newVersionCommitMessage = newVersionCommitMessage
            .replace(DEPLOYMENT_TICKET_PLACEHOLDER, deploymentTicket)
            .replace(NEW_VERSION_PLACEHOLDER, newVersion)
    )
}

private fun createTagInCloudConfigRepo(git: Git, cloudConfigMainBranch: String, tagName: String, preReleaseCommitMessage: String) {
    checkout(git, cloudConfigMainBranch)
    createTag(git, tagName, preReleaseCommitMessage)
    push(git, tagName)
}

private fun pushNewProjectVersion(
    git: Git,
    projectRepoPath: String,
    gradlePropertiesFilePath: String,
    applicationMainBranch: String,
    newVersionCommitMessage: String,
) {
    add(git, Paths.get(projectRepoPath).toRealPath().relativize(Paths.get(gradlePropertiesFilePath).toRealPath()).toString())//TODO
    commit(git, newVersionCommitMessage)
    push(git, applicationMainBranch)
}