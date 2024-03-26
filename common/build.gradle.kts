architectury {
    common("forge", "fabric")
    platformSetupLoomIde()
}

val minecraft_version = project.properties["minecraft_version"] as String

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation ("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")

    modApi("com.cobblemon:mod:${project.properties["cobblemon_version"]}+$minecraft_version")
    modApi("com.github.Virtuoel:Pehkui:${project.properties["pehkui_version"]}-$minecraft_version-forge")
    //modApi("org.valkyrienskies:valkyrienskies-118-common:${project.properties["vs2_version"]}")
    
    //Networking - CommonNetworking
    modApi ("mysticdrew:common-networking-common:${project.properties["common_networking_version"]}-$minecraft_version")
    
}


tasks {

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

}
