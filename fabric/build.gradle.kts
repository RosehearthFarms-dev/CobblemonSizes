plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val minecraft_version = project.properties["minecraft_version"] as String
val fabric_version = project.properties["fabric_api_version"] as String

sourceSets.main.get().resources.srcDir(file("src/main/generated/resources"))

configurations {
    create("common")
    create("shadowCommon")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentFabric").extendsFrom(configurations["common"])
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")
    modApi("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_api_version"]}+$minecraft_version")
    modApi("dev.architectury:architectury-fabric:${project.properties["architectury_version"]}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowCommon"(project(":common", "transformProductionFabric")) { isTransitive = false }

    //Cobblemon
    modApi("com.cobblemon:fabric:${project.properties["cobblemon_version"]}+$minecraft_version")

    
    //Networking - CommonNetworking
    modImplementation ("mysticdrew:common-networking-fabric:${project.properties["common_networking_version"]}-$minecraft_version")
    
   // modCompileOnly ("dev.emi:emi-fabric:${project.properties["emi_version"]}+$minecraft_version:api")
   // modLocalRuntime ("dev.emi:emi-fabric:${project.properties["emi_version"]}+$minecraft_version")
}

tasks {
    base.archivesName.set(base.archivesName.get() + "-fabric")
    processResources {
        val filesToProcess = listOf("pack.mcmeta", "*/mods.toml", "*.mixins.json","fabric.mod.json")

        inputs.property("version", project.properties["mod_version"])
        inputs.property("modId", project.properties["mod_id"])
        inputs.property("displayName", project.properties["mod_name"])
        inputs.property("authors", project.properties["mod_authors"])
        inputs.property("description", project.properties["mod_description"])
        inputs.property("license", project.properties["mod_license"])
        inputs.property("mod_issue_url", project.properties["mod_issue_url"])
        inputs.property("mod_homepage", project.properties["mod_homepage"])

        filesMatching(filesToProcess) {
            expand(
                "version" to  project.properties["mod_version"],
                "modId" to project.properties["mod_id"],
                "displayName" to project.properties["mod_name"],
                "authors" to project.properties["mod_authors"],
                "description" to project.properties["mod_description"],
                "license" to project.properties["mod_license"],
                "mod_issue_url" to project.properties["mod_issue_url"],
                "mod_homepage" to project.properties["mod_homepage"]
            )
        }
    }

    shadowJar {
        exclude ("architectury.common.json")
        exclude("generations/gg/generations/core/generationscore/fabric/datagen/**")
        exclude("data/forge/**")
        configurations = listOf(project.configurations.getByName("shadowCommon"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }

    jar.get().archiveClassifier.set("dev")

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
    }
}

components {
    java.run {
        if (this is AdhocComponentWithVariants)
            withVariantsFromConfiguration(project.configurations.shadowRuntimeElements.get()) { skip() }
    }
}
