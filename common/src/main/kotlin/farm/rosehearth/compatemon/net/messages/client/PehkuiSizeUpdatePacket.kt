package farm.rosehearth.compatemon.net.messages.client

import com.cobblemon.mod.common.pokemon.Pokemon
import farm.rosehearth.compatemon.modules.compatemon.IPokemonExtensions
import farm.rosehearth.compatemon.util.CompateUtils
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf

class PehkuiSizeUpdatePacket(pokemon: () -> Pokemon, value: CompoundTag) : PersistentDataUpdatePacket<PehkuiSizeUpdatePacket>(pokemon, value){
	override val id = ID
	override fun set(pokemon: Pokemon, value: CompoundTag) {
		(pokemon as IPokemonExtensions).`compatemon$setPersistentData`(value)
	}
	companion object {
		val ID = CompateUtils.id("pehkui_scale_update")
		fun decode(buffer: FriendlyByteBuf) = buffer.readNbt()
			?.let { PehkuiSizeUpdatePacket(decodePokemon(buffer), it) }
	}
	
}