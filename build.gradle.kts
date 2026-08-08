plugins {
    java
}

group = project.property("group") as String
version = project.property("version") as String

repositories {
    mavenCentral()

    // Paper / Purpur
    maven("https://repo.papermc.io/repository/maven-public/")

    // Geyser / Floodgate
    maven("https://repo.opencollab.dev/main/")

    // CodeMC
    maven("https://repo.codemc.io/repository/maven-public/")

    // BentoBox
    maven("https://repo.codemc.org/repository/bentoboxworld/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("org.geysermc.floodgate:api:2.2.5-SNAPSHOT")
    compileOnly("world.bentobox:bentobox:3.20.0")
    compileOnly("net.luckperms:api:5.4")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand(
            "version" to project.version
        )
    }
}
