plugins {
    id("net.fabricmc.fabric-loom") version "1.17.17"
}

base {
    archivesName = project.property("archives_base_name").toString()
    version = project.property("mod_version").toString()
    group = project.property("maven_group").toString()
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.terraformersmc.com/releases/") {
        name = "Terraformers"
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    // Compile only: Mod Menu is optional at runtime, the integration class is
    // never loaded when it is absent.
    compileOnly("com.terraformersmc:modmenu:${project.property("modmenu_version")}") {
        isTransitive = false
    }
}

tasks {
    processResources {
        val propertiesMap = mapOf(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version"),
            "loader_version" to project.property("loader_version")
        )

        inputs.properties(propertiesMap)
        filesMatching("fabric.mod.json") {
            expand(propertiesMap)
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 25
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
}
