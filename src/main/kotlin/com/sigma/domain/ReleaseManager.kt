package com.sigma.domain

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.nio.file.Paths

const val DEPLOYMENT_TICKET_PLACEHOLDER = "{deploymentTicket}"

fun preBuild(deploymentTicket: String, projectRepoPath: String, releaseBranchName: String, bootstrapFile: String, tagName: String, preReleaseCommitMessage: String) {
    val git = createGit(projectRepoPath)

    checkout(git, releaseBranchName, createNewBranch = true)
    addSpringCloudConfigLabel(bootstrapFile = bootstrapFile, tagName)
    add(git, Paths.get(projectRepoPath).parent.relativize(Paths.get(bootstrapFile)).toString())
    commit(git, preReleaseCommitMessage.replace(DEPLOYMENT_TICKET_PLACEHOLDER, deploymentTicket))
    push(git, releaseBranchName)
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

    createTagInCloudConfigRepo(cloudConfigGit, cloudConfigMainBranch, tagName, preReleaseCommitMessage.replace(DEPLOYMENT_TICKET_PLACEHOLDER, deploymentTicket))
    updateProjectVersion(projectGit, projectRepoPath, gradlePropertiesFilePath, applicationMainBranch, newVersionCommitMessage.replace(DEPLOYMENT_TICKET_PLACEHOLDER, deploymentTicket))
}

private fun createGit(repoPath: String) = Git(
    FileRepositoryBuilder()
        .setGitDir(File(repoPath))
        .readEnvironment() // scan environment GIT_* variables
        .findGitDir() // scan up the file system tree
        .build()
)

private fun createTagInCloudConfigRepo(git: Git, cloudConfigMainBranch: String, tagName: String, preReleaseCommitMessage: String) {
    checkout(git, cloudConfigMainBranch)
    createTag(git, tagName, preReleaseCommitMessage)
    push(git, tagName)
}

private fun updateProjectVersion(git: Git, projectRepoPath: String, gradlePropertiesFilePath: String, applicationMainBranch: String, newVersionCommitMessage: String) {
    checkout(git, applicationMainBranch)
    val newVersion = incrementAppVersion(gradlePropertiesFilePath)
    add(git, Paths.get(projectRepoPath).parent.relativize(Paths.get(gradlePropertiesFilePath)).toString())
    commit(git, newVersionCommitMessage.replace("{newValue}", newVersion))
    push(git, applicationMainBranch)
}