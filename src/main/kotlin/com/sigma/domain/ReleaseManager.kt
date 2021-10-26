package com.sigma.domain

import org.eclipse.jgit.api.Git

const val MASTER = "master"

fun preBuild(git: Git, releaseBranch: String, preReleaseCommitMessage: String, bootstrapFile: String) {
    addSpringCloudConfigLabel(bootstrapFile = bootstrapFile, releaseBranch)
    checkout(git, releaseBranch, createNewBranch = true)
    commit(git, preReleaseCommitMessage)
    push(git, releaseBranch)
}

fun postBuild(projectGit: Git,
              cloudConfigGit: Git,
              tagName: String,
              preReleaseCommitMessage: String,
              newVersionCommitMessage: String) {
    createTagInCloudConfigRepo(cloudConfigGit, tagName, preReleaseCommitMessage)
    updateProjectVersion(projectGit, newVersionCommitMessage)
}

fun createTagInCloudConfigRepo(git: Git, tagName: String, preReleaseCommitMessage: String) {
    checkout(git, MASTER)
    createTag(git, tagName, preReleaseCommitMessage)
    push(git, tagName)
}

fun updateProjectVersion(git: Git, newVersionCommitMessage: String) {
    checkout(git, MASTER)
    incrementAppVersion()
    add(git, GRADLE_PROPERTIES_FILE)
    commit(git, newVersionCommitMessage)
    push(git, MASTER)
}

