package com.sigma.domain

class ReleaseManager(
    val ticketNumber: String,
    val version: String,
    val preReleaseCommitMessage: String,
    val newVersionCommitMessage: String
) {
    fun preBuild() {
        //TODO
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