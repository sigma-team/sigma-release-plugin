package com.sigma.domain

import com.sigma.domain.IncrementStrategy.MINOR
import org.apache.commons.configuration.PropertiesConfiguration

const val GRADLE_PROPERTIES_FILE = "gradle.properties"

fun incrementGradleVersion(
        propertiesFile: String = GRADLE_PROPERTIES_FILE,
        incrementStrategy: IncrementStrategy = MINOR
) {
    val version = getPropertyValue(propertiesFile, "version")
    val incrementedVersion = incrementStrategy.increment(version)
    updatePropertyFile(propertiesFile, "version", incrementedVersion)
}

private fun updatePropertyFile(filePath: String,
                       propertyKey: String,
                       propertyValue: String
) = with(PropertiesConfiguration(filePath)) {
    setProperty(propertyKey, propertyValue)
    save()
}

private fun getPropertyValue(filePath: String,
                     propertyKey: String
) = PropertiesConfiguration(filePath).getString(propertyKey)!!

enum class IncrementStrategy(private val versionIndex: Int) {
    MAJOR(0),
    MINOR(1),
    PATCH(2);

    internal fun increment(version: String) = with(Regex("(\\d+)[.]*\\.(\\d+)\\.(\\d+)[.]*")) {
        find(version)!!
                .value
                .split(".")
                .toMutableList()
                .let {
                    it[versionIndex] = (it[versionIndex].toInt() + 1).toString()
                    version.replace(this, it.joinToString("."))
                }
    }

}