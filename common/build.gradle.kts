architectury {
    common("forge", "fabric")
    platformSetupLoomIde()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // Cobblemon, Pehkui, ClothConfig
    // Cloth Config no longer needed

    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation ("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")
    // modApi ("dev.architectury:architectury:${project.properties["architectury_version"]}")
    // modImplementation("dev.architectury:architectury:${project.properties["architectury_version"]}")

    modApi("com.cobblemon:mod:${project.properties["cobblemon_version"]}+${project.properties["minecraft_version"]}")
    modApi("com.github.Virtuoel:Pehkui:${project.properties["pehkui_version"]}-${project.properties["minecraft_version"]}-forge")
    modApi("me.shedaniel.cloth:cloth-config:${project.properties["cloth_config_version"]}")
    //modApi("org.valkyrienskies:valkyrienskies-118-common:${project.properties["vs2_version"]}")
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
