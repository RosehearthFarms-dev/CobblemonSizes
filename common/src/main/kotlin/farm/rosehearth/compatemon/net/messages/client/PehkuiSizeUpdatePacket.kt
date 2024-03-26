package farm.rosehearth.compatemon.net.messages.client

import com.cobblemon.mod.common.api.abilities.Abilities
import com.cobblemon.mod.common.api.abilities.AbilityTemplate
import com.cobblemon.mod.common.net.messages.client.pokemon.update.SingleUpdatePacket
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.cobblemonResource
import farm.rosehearth.compatemon.modules.compatemon.IPokemonExtensions
import farm.rosehearth.compatemon.util.CompateUtils
import net.minecraft.network.FriendlyByteBuf

class PehkuiSizeUpdatePacket(pokemon: () -> Pokemon, value: Float) : FloatUpdatePacket<PehkuiSizeUpdatePacket>(pokemon, value){
	override val id = ID
	override fun set(pokemon: Pokemon, value: Float) {
		(pokemon as IPokemonExtensions).`compatemon$setPokemonScale`(value)
	}
	companion object {
		val ID = CompateUtils.id("pehkui_scale_update")
		fun decode(buffer: FriendlyByteBuf) = PehkuiSizeUpdatePacket(decodePokemon(buffer), buffer.readFloat())
	}
	
}