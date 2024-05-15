package farm.rosehearth.compatemon.net.messages.client

import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.net.messages.client.pokemon.update.SingleUpdatePacket
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf

abstract class PersistentDataUpdatePacket<T : NetworkPacket<T>>(pokemon: () -> Pokemon, value: CompoundTag) : SingleUpdatePacket<CompoundTag, T>(pokemon, value) {
	
	override fun encodeValue(buffer: FriendlyByteBuf) {
		buffer.writeNbt(this.value)
	}
}
