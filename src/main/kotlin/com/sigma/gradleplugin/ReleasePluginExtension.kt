package com.sigma.gradleplugin

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

abstract class ReleasePluginExtension(objectFactory: ObjectFactory) {

    val ticketNumber = objectFactory.property(String::class.java)

    val version = objectFactory.property(String::class.java)

    val preReleaseCommitMessage = objectFactory.property(String::class.java)
        .convention("$ticketNumber: [Release process] Pre release commit, version: '$version'")

    val newVersionCommitMessage = objectFactory.property(String::class.java)
        .convention("$ticketNumber: [Release process] Changed version number to '$version'")
}