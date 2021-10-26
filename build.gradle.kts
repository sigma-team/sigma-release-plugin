plugins {
    `kotlin-dsl`
    `maven-publish`
}

group "org.amobee"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("codegen-plugin") {
            description = "TODO"
            displayName = "Sigma release plugin"
            id = "sigma-release-plugin"
            implementationClass = "com.sigma.gradleplugin.SigmaReleasePlugin"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    implementation ("org.eclipse.jgit:org.eclipse.jgit:5.13.0.202109080827-r")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {
    jar {
        archiveBaseName.set("sigma-team.sigma-release-plugin")
    }

    test {
        useJUnitPlatform()
    }
}