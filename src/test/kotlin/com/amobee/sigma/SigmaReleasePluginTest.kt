package com.amobee.sigma

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class SigmaReleasePluginTest {
    @Test
    fun testApply() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("sigma-release-plugin")
        //Assertions.assertNotNull(project.getTasks().getByName("hello"));
    }
}