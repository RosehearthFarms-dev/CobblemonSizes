import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {

    id("java")
    id("java-library")
    id("architectury-plugin") version "3.4-SNAPSHOT"
    kotlin("jvm") version ("1.9.10")
    id("dev.architectury.loom") version "1.3.357" apply false
    idea
}

val minecraft_version = project.properties["minecraft_version"] as String

architectury.minecraft = minecraft_version

subprojects {
    apply(plugin = "dev.architectury.loom")
    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://maven.impactdev.net/repository/development/")
        maven("https://maven.parchmentmc.org")
        maven("https://maven.terraformersmc.com/")
        maven("https://api.modrinth.com/maven")
        maven("https://www.cursemaven.com")
        maven( "https://repo.spongepowered.org/repository/maven-public/" )
        maven("https://maven.shedaniel.me/")
        maven("https://maven.blamejared.com")
        maven("https://jm.gserv.me/repository/maven-public/")
        exclusiveContent {
            forRepository {
                maven("https://api.modrinth.com/maven")
            }
            filter {
                includeGroup("maven.modrinth")
            }
        }
    }

    @Suppress("UnstableApiUsage")
    dependencies {
        "minecraft"("com.mojang:minecraft:$minecraft_version")
        "mappings"(loom.layered{
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-$minecraft_version:${project.properties["parchment"]}@zip")
        })

        compileOnly("org.jetbrains:annotations:${project.properties["jetbrains_annotations_version"]}")
    }
    loom.silentMojangMappingsLicense()
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    version = project.properties["mod_version"] as String
    group = project.properties["maven_group"] as String
    base.archivesName.set(project.properties["mod_id"] as String)

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    java.withSourcesJar()

}

tasks{
    processResources{
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
}

kotlin.jvmToolchain(17)
dependencies {
    implementation(kotlin("stdlib"))
}
repositories {
    mavenCentral()
}