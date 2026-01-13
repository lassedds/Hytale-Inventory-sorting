plugins {
    java
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = property("pluginGroup") as String
version = property("pluginVersion") as String

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
    maven("https://repo.hytale.com/releases") // Hytale Maven repository
}

dependencies {
    // Hytale Server API - provided at runtime
    compileOnly("com.hypixel.hytale:Server:+")

    // Annotations for documentation
    compileOnly("org.jetbrains:annotations:24.1.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(25)
}

tasks.shadowJar {
    archiveClassifier.set("")
    minimize()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.processResources {
    filesMatching("manifest.json") {
        expand(
            "version" to version,
            "description" to property("pluginDescription")
        )
    }
}
