package farm.rosehearth.compatemon


import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_ENTITY_LOAD
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_ENTITY_SAVE
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_ENTITY_SPAWN
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_SENT_POST
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_SENT_PRE
import commonnetwork.api.Network
import farm.rosehearth.compatemon.events.CompatemonEvents.POKEMON_SENT_N_SPAWNED
import farm.rosehearth.compatemon.modules.pehkui.util.CompatemonScaleUtils.Companion.getScale
import farm.rosehearth.compatemon.modules.pehkui.util.CompatemonScaleUtils.Companion.setScale
import farm.rosehearth.compatemon.network.packet.PokemonSizePacket
import farm.rosehearth.compatemon.util.CompatemonDataKeys.APOTH_BOSS
import farm.rosehearth.compatemon.util.CompatemonDataKeys.APOTH_RARITY_COLOR
import farm.rosehearth.compatemon.util.CompatemonDataKeys.COMPAT_SCALE_SIZE
import farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_APOTHEOSIS
import farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_COMPATEMON
import farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_PEHKUI
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.world.entity.Entity

object CompatemonKotlin {

    fun initialize() {

        // Occurs when the Pokemon is saved to the world or party
        POKEMON_ENTITY_SAVE.subscribe{ event ->
            if(Compatemon.ShouldLoadMod(MOD_ID_APOTHEOSIS)){ // Save boss data to the pokemon
                var isBoss = event.pokemonEntity.pokemon.persistentData.getCompound(MOD_ID_COMPATEMON).contains(APOTH_BOSS);
                if(isBoss){
                    var rarityKey = event.pokemonEntity.pokemon.persistentData.getCompound(MOD_ID_COMPATEMON).getString(APOTH_RARITY_COLOR)
                    event.pokemonEntity.pokemon.nickname = event.pokemonEntity.pokemon.nickname?.withStyle(Style.EMPTY.withColor(TextColor.parseColor(rarityKey)))
                }
            }
        }

        POKEMON_ENTITY_LOAD.subscribe{ event ->
            if(Compatemon.ShouldLoadMod(MOD_ID_PEHKUI)) { // set the scale when the world is loaded
                Compatemon.LOGGER.info("We're setting the scale at load time!")
                var scale = setScale(event.pokemonEntity, COMPAT_SCALE_SIZE)
                Compatemon.LOGGER.info("WE SET THE load SCALE TO : {}", scale )
              // ( event.pokemonEntity.pokemon.form as IScalableFormData).`compatemon$setSizeScale`(scale)
            }
        }

        POKEMON_ENTITY_SPAWN.subscribe{event ->
            if(Compatemon.ShouldLoadMod(MOD_ID_PEHKUI)) {
                Compatemon.LOGGER.info("POKEMON_ENTITY_SPAWN FIRED: {}", event.entity.pokemon.species.name )

                var scale = setScale(event.entity, COMPAT_SCALE_SIZE)
                Compatemon.LOGGER.info("WE SET THE SPAWNED SCALE TO : {}", scale )
            }
        }

        POKEMON_SENT_PRE.subscribe{event ->
            Compatemon.LOGGER.debug("POKEMON_SENT_PRE  FIRED: {}", event.pokemon.species.name )
        }
        POKEMON_SENT_POST.subscribe{event ->
            Compatemon.LOGGER.debug("POKEMON_SENT_POST FIRED: {}", event.pokemon.species.name )
        }

        // This occurs after the Sent Pre Event has started and the entity has already been instantiated
        // Only fires SERVER side
        POKEMON_SENT_N_SPAWNED.subscribe { event ->
            
            Compatemon.LOGGER.info("POKEMON_SENT_N_SPAWNED FIRED: {}", event.pokemon.species.name )

            var scale = setScale(event.pokemon.entity as Entity, COMPAT_SCALE_SIZE)
            var nscale = getScale(event.pokemon.entity as Entity, COMPAT_SCALE_SIZE)
            var escale = getScale(event.pokemonEntity as Entity, COMPAT_SCALE_SIZE)
            
            Network.getNetworkHandler().sendToClientsInLevel(PokemonSizePacket(event.pokemon, nscale), event.level)
            Compatemon.LOGGER.info("Sent out with a scale of : {}", scale )
            Compatemon.LOGGER.info("Here are the scales for the pokemon's entity and the pokemonentity: {}, {}", nscale, escale )

        }
    }
}