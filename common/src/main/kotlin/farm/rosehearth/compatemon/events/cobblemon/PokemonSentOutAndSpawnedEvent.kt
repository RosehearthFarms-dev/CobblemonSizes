package farm.rosehearth.compatemon.events.cobblemon

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3

data class PokemonSentOutAndSpawnedEvent(
	val pokemon: Pokemon,
	val pokemonEntity: PokemonEntity?,
	val level: ServerLevel,
	val position: Vec3
): Cancelable()
