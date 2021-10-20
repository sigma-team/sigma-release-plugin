package com.sigma.domain

import org.gradle.api.tasks.Input

class ReleaseManager(
    val ticketNumber: String,
    val version: String,
    val preReleaseCommitMessage: String,
    val newVersionCommitMessage: String
) {
    fun createTagInCloudConfigRepo() {
        //TODO
    }

    fun updateVersion() {
        //TODO
    }

    fun createTagInProjectRepo() {
        //TODO
    }

    fun doPreReleaeActions() {
        //TODO
    }

}