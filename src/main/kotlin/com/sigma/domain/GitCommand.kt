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