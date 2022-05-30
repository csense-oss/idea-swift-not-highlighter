plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.intellij") version "1.6.0"
}

group = "csense-idea"
version = "1.1"

repositories {
    mavenCentral()
    maven {
        setUrl("https://pkgs.dev.azure.com/csense-oss/csense-oss/_packaging/csense-oss/maven/v1")
        name = "Csense oss"
    }
}

dependencies {
    implementation("csense.kotlin:csense-kotlin-jvm:0.0.59")
    implementation("csense.kotlin:csense-kotlin-annotations-jvm:0.0.41")
    implementation("csense.kotlin:csense-kotlin-datastructures-algorithms:0.0.41")
}


// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    updateSinceUntilBuild.set(false)
    intellij.localPath.set("")//appcode path
    plugins.set(listOf("com.intellij.appcode"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        changeNotes.set(
            """
                <ul>
                    <li>Fixed a bad name (code is still the same as in version 1.0)</li>
                </ul>
              """
        )
    }

    runIde {
        jvmArgs("-Xms2G")
        jvmArgs("-Xmx8G")
    }
}