package com.amobee.sigma

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class SigmaReleasePluginTest {

    @TempDir
    lateinit var tempDir: Path
    lateinit var testGradleProjectDir: Path

    @BeforeEach
    internal fun setUp() {
        testGradleProjectDir = tempDir.resolve("target-project")
        Files.createDirectories(testGradleProjectDir)
        testGradleProjectDir.resolve("settings.gradle").toFile().writeText("rootProject.name = \"target-project\"")
        testGradleProjectDir.resolve("build.gradle").toFile()
            .writeText(
                """
                    plugins {
                        id 'com.sigma.release'
                        id 'java'
                    }
                    
                    sigmaReleasePlugin {
                        deploymentTicket = "deploymentTicket"
                        bootstrapFilePath = "bootstrapFilePath"
                        tagName = "tagName"
                        releaseBranchName = "releaseBranchName"
                        applicationMainBranch = "applicationMainBranch"
                        preReleaseCommitMessage = "preReleaseCommitMessage"
                        newVersionCommitMessage = "newVersionCommitMessage"
                    }
                    """
            )
    }

    @Test
    fun `When plugin is applied then the tasks is created`() {
        val project = ProjectBuilder.builder()
            .withProjectDir(testGradleProjectDir.toFile())
            .build()
        project.pluginManager.apply("com.sigma.release")
        Assertions.assertNotNull(project.tasks.getByName("preBuildRelease"))
        Assertions.assertNotNull(project.tasks.getByName("postBuildRelease"))
    }

    @Disabled
    @Test
    fun `When the build task is run then the 'preBuild' and 'postBuild' tasks must be run`() {
        val result = GradleRunner.create()
            .withProjectDir(testGradleProjectDir.toFile())
            .withArguments("build")
            .withPluginClasspath()
            .build()

        Assertions.assertTrue(result.output.contains("run sigma release plugin `preBuild` step"))
        Assertions.assertTrue(result.output.contains("run sigma release plugin `postBuild` step"))
        Assertions.assertTrue(result.task(":build")?.outcome == TaskOutcome.SUCCESS)
    }
}