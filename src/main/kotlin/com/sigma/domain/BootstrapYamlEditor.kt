package com.sigma.domain

import java.io.File

const val DEFAULT_BOOTSTRAP_FILE = "src/main/resources/bootstrap.yml"

fun addSpringCloudConfigLabel(value: String) {
    addSpringCloudConfigLabel(DEFAULT_BOOTSTRAP_FILE, value)
}

fun addSpringCloudConfigLabel(bootstrapFile: String, value: String) {
    add(bootstrapFile, "spring.cloud.config.label", value)
}

fun add(bootstrapFile: String, property: String, value: String) {
    File(bootstrapFile).appendText("\n$property: $value")
}