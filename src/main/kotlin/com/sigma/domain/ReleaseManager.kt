package com.sigma.domain

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

fun preBuild(projectRepoPath: String, releaseBranchName: String, bootstrapFile: String, tagName: String, preReleaseCommitMessage: String) {
    val git = createGit(projectRepoPath)

    checkout(git, releaseBranchName, createNewBranch = true)
    addSpringCloudConfigLabel(bootstrapFile = bootstrapFile, tagName)
    commit(git, preReleaseCommitMessage)
    push(git, releaseBranchName)
}

fun postBuild(
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

    createTagInCloudConfigRepo(cloudConfigGit, cloudConfigMainBranch, tagName, preReleaseCommitMessage)
    updateProjectVersion(projectGit, gradlePropertiesFilePath, applicationMainBranch, newVersionCommitMessage)
}

private fun createGit(repoPath: String) = Git(
    FileRepositoryBuilder()
        .setGitDir(File(repoPath))
        .readEnvironment() // scan environment GIT_* variables
        .findGitDir() // scan up the file system tree
        .build()
)

private fun createTagInCloudConfigRepo(git: Git, cloudConfigMainBranch:String, tagName: String, preReleaseCommitMessage: String) {
    checkout(git, cloudConfigMainBranch)
    createTag(git, tagName, preReleaseCommitMessage)
    push(git, tagName)
}

private fun updateProjectVersion(git: Git, gradlePropertiesFilePath: String, applicationMainBranch: String, newVersionCommitMessage: String) {
    checkout(git, applicationMainBranch)
    incrementAppVersion(gradlePropertiesFilePath)
    add(git, gradlePropertiesFilePath)
    commit(git, newVersionCommitMessage)
    push(git, applicationMainBranch)
}

