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

    // PlaceholderAPI
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    // Vault（VaultAPI 掛在 JitPack 上）
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("org.geysermc.floodgate:api:2.2.5-SNAPSHOT")
    compileOnly("world.bentobox:bentobox:3.22.2")
    compileOnly("world.bentobox:warps:1.19.1-SNAPSHOT")
    compileOnly("world.bentobox:challenges:1.8.0-SNAPSHOT")
    compileOnly("world.bentobox:bank:1.10.1-SNAPSHOT")
    compileOnly("world.bentobox:visit:1.7.0-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.12.3")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        // VaultAPI 1.7 本身依賴一個很舊的 org.bukkit:bukkit:1.13.1，
        // 跟 paper-api:26.2 互相衝突導致 Gradle 無法解析依賴。
        // 我們只需要 Vault 的介面（Economy 等），排除它附帶的舊 Bukkit。
        exclude(group = "org.bukkit", module = "bukkit")
    }

    // 測試只涵蓋不依賴伺服器實例、純邏輯的 util/reflection class，
    // 用 paper-api 提供 ChatColor 等不需要真正伺服器就能跑的靜態工具類別。
    testImplementation("io.papermc.paper:paper-api:26.2.build.+")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
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
