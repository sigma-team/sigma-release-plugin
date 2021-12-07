package com.amobee.sigma

import com.sigma.domain.add
import com.sigma.domain.checkout
import com.sigma.domain.commit
import com.sigma.domain.createGit
import com.sigma.domain.push
import org.eclipse.jgit.api.Git
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.nio.file.Path


class SigmaReleasePluginTest {

    private val deploymentTicket = "ROSE-0001"

    @TempDir
    private lateinit var tempDir: Path
    private lateinit var projectRepoDir: Path
    private lateinit var cloudConfigRepoDir: Path

    @BeforeEach
    internal fun setUp() {
        val projectRepo = initializeTempRepository("app-project")
        val cloudConfigRepo = initializeTempRepository("cloudConfig-project")
        projectRepoDir = projectRepo.repository.directory.toPath().parent
        cloudConfigRepoDir = cloudConfigRepo.repository.directory.toPath().parent

        initializeProjectFiles(projectRepo)

        projectRepoDir.resolve("settings.gradle").toFile().writeText("rootProject.name = \"app-name\"")
        projectRepoDir.resolve("build.gradle").toFile()
            .writeText(
                """
                    plugins {
                        id 'com.sigma.release'
                        id 'java'
                    }
                    
                    sigmaReleasePlugin {
                        projectRepoPath = "$projectRepoDir"
                        cloudConfigRepoPath = "$cloudConfigRepoDir"
                        deploymentTicket = "$deploymentTicket"
                    }
                    """
            )
    }

    @Test
    fun `When plugin is applied then the tasks is created`() {
        val project = ProjectBuilder.builder()
            .withProjectDir(projectRepoDir.toFile())
            .build()
        project.pluginManager.apply("com.sigma.release")
        Assertions.assertNotNull(project.tasks.getByName("preBuildRelease"))
        Assertions.assertNotNull(project.tasks.getByName("postBuildRelease"))
    }

    //    @Disabled
    @Test
    fun `When the build task is run then the 'preBuild' and 'postBuild' tasks must be run`() {
        val result = GradleRunner.create()
            .withProjectDir(projectRepoDir.toFile())
            .withArguments("build")
            .withDebug(true)
            .withPluginClasspath()
            .forwardStdError(System.out.writer())
            .build()

        Assertions.assertTrue(result.output.contains("run sigma release plugin `preBuild` step"))
        Assertions.assertTrue(result.output.contains("run sigma release plugin `postBuild` step"))
        Assertions.assertTrue(result.task(":build")?.outcome == TaskOutcome.SUCCESS)
        assertCommitInAppRepo()
        assertCommitInCloudConfigRepo()
        assertBootstrapYml()
        assertGradleProperties()
    }

    private fun assertGradleProperties() {
        val git = createGit(projectRepoDir.toString())
        checkout(git, "master")
        val fileContent = File("$projectRepoDir/gradle.properties")
            .inputStream()
            .bufferedReader()
            .use { it.readText() }
        Assertions.assertTrue(fileContent.contains("version=1.1.0-SNAPSHOT"))
    }

    private fun assertBootstrapYml() {
        val git = createGit(projectRepoDir.toString())
        checkout(git, "release-1.0.0")
        val fileContent = File("$projectRepoDir/src/main/resources/bootstrap.yml")
            .inputStream()
            .bufferedReader()
            .use { it.readText() }
        Assertions.assertTrue(fileContent.contains("spring.cloud.config.label: app-name-1.0.0"))
    }

    private fun assertCommitInAppRepo() {
        val git = createGit(projectRepoDir.toString())
        val releaseBranch = "release-1.0.0"
        val preReleaseCommitMessage = git.log().add(git.repository.resolve(releaseBranch)).call().iterator().next().fullMessage
        val newVersionCommitMessage = git.log().call().iterator().next().fullMessage
        Assertions.assertEquals("$deploymentTicket: [Release process] Pre release commit, version: '1.0.0'", preReleaseCommitMessage)
        Assertions.assertEquals("$deploymentTicket: [Release process] Changed version number to '1.1.0-SNAPSHOT'", newVersionCommitMessage)
    }

    private fun assertCommitInCloudConfigRepo() {
        val cloudConfigRepo = createGit(cloudConfigRepoDir.toString())
        val lastCommit = cloudConfigRepo.log().setMaxCount(1).call().iterator().next()
        val namedCommits = cloudConfigRepo.nameRev().addPrefix("refs/tags/").add(lastCommit).call()
        val tag = namedCommits[lastCommit.id]
        Assertions.assertEquals(tag, "app-name-1.0.0")
    }


    private fun initializeTempRepository(repositoryName: String): Git {
        val originRepoDir = tempDir.resolve("$repositoryName-origin.git").also { Files.createDirectories(it) }
        val originRepo = Git.init().setDirectory(originRepoDir.toFile()).setBare(true).call().repository
        val repository = Git.cloneRepository().setURI(originRepo.directory.absolutePath).setDirectory(tempDir.resolve("$repositoryName").toFile()).call()
        commit(repository, "initial commit")
        push(repository, "master")
        return repository
    }

    private fun initializeProjectFiles(projectRepo: Git) {
        val bootstrapFilePath = projectRepoDir.resolve("src").resolve("main").resolve("resources").resolve("bootstrap.yml")
        val gradlePropertiesFilePath = projectRepoDir.resolve("gradle.properties")
        getResource("bootstrap.yml").copyTo(bootstrapFilePath.toFile())
        getResource("gradle.properties").copyTo(gradlePropertiesFilePath.toFile())

        add(projectRepo, projectRepoDir.relativize(bootstrapFilePath).toString())
        add(projectRepo, projectRepoDir.relativize(gradlePropertiesFilePath).toString())
        commit(projectRepo, "added requiredFiles")
        push(projectRepo, "master")
    }

    private fun getResource(name: String): File {
        return this.javaClass.classLoader.getResource(name).let { File(it.toURI()) }
    }
}