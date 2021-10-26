package com.sigma.domain

import org.eclipse.jgit.api.Git

class ReleaseManager(
    var git: Git,
    val tagName: String,
    val releaseBranch: String,
    val preReleaseCommitMessage: String,
    val newVersionCommitMessage: String,
    val bootstrapFile: String
) {
    fun preBuild() {
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

}