package farm.rosehearth.compatemon

import com.cobblemon.mod.common.api.data.JsonDataRegistry
import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import com.mojang.brigadier.arguments.ArgumentType
import kotlin.reflect.KClass
import net.minecraft.resources.ResourceLocation
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

