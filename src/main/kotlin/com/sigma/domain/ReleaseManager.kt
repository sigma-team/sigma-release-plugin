package com.sigma.domain

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

fun preBuild(projectRepoPath: String, releaseBranchName: String, bootstrapFile: String, tagName: String, preReleaseCommitMessage: String) {
    val git = createGit(projectRepoPath)

    addSpringCloudConfigLabel(bootstrapFile = bootstrapFile, tagName)
    checkout(git, releaseBranchName, createNewBranch = true)
    commit(git, preReleaseCommitMessage)
    push(git, releaseBranchName)
}

fun postBuild(projectRepoPath: String,
              cloudConfigRepoPath: String,
              applicationMainBranch: String,
              cloudConfigMainBranch: String,
              tagName: String,
              preReleaseCommitMessage: String,
              newVersionCommitMessage: String) {
    val projectGit = createGit(projectRepoPath)
    val cloudConfigGit = createGit(cloudConfigRepoPath)

    createTagInCloudConfigRepo(cloudConfigGit, cloudConfigMainBranch, tagName, preReleaseCommitMessage)
    updateProjectVersion(projectGit, applicationMainBranch, newVersionCommitMessage)
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

private fun updateProjectVersion(git: Git, applicationMainBranch: String, newVersionCommitMessage: String) {
    checkout(git, applicationMainBranch)
    incrementAppVersion()
    add(git, GRADLE_PROPERTIES_FILE)
    commit(git, newVersionCommitMessage)
    push(git, applicationMainBranch)
}

