package com.sigma.domain

import org.eclipse.jgit.api.Git

fun preBuild(git: Git, releaseBranch: String, preReleaseCommitMessage: String, bootstrapFile: String) {
    addSpringCloudConfigLabel(bootstrapFile = bootstrapFile, releaseBranch)
    checkout(git, releaseBranch, createNewBranch = true)
    commit(git, preReleaseCommitMessage)
    push(git, releaseBranch)
}

fun postBuild() {
    //TODO
}

fun createTagInCloudConfigRepo() {
    //TODO
}

fun updateVersion() {
    //TODO
}

fun createTagInProjectRepo() {
    //TODO
}

fun doPreReleaseActions() {
    //TODO
}