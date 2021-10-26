package com.sigma.domain

import org.eclipse.jgit.api.Git


fun checkout(git: Git,
             destination: String,
             createNewBranch: Boolean = false,
             forceCheckout: Boolean = false
) = git.checkout()
        .setName(destination)
        .setForced(forceCheckout)
        .setCreateBranch(createNewBranch)
        .call()

fun commit(git: Git,
           message: String
) = git.commit()
        .setMessage(message)
        .call()

fun add(git: Git,
        vararg filePatterns: String) =
        git.add().apply {
            filePatterns.forEach { this.addFilepattern(it) }
        }.call()