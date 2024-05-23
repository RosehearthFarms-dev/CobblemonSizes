package farm.rosehearth.compatemon

import net.minecraft.server.MinecraftServer

/**
 * TY Cobblemon for your well documented repository!
 *
 */
interface CompatemonImplementation {

	val modAPI: ModAPI
	fun isModInstalled(modID: String): Boolean

	fun postCommonInitialization()

	fun registerEvents()

	fun persistentDataKey():String
	fun environment(): Environment
	fun server(): MinecraftServer?
}

enum class ModAPI {
	FABRIC,
	FORGE
}

enum class Environment {
	CLIENT,
	SERVER

}

