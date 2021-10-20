package com.amobee.sigma;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SigmaReleasePluginTest {
    @Test
    public void testApply() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("sigmaReleasePlugin");
        Assertions.assertNotNull(project.getTasks().getByName("hello"));
    }
}
