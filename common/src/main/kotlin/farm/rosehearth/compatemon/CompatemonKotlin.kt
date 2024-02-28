package farm.rosehearth.compatemon


import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_ENTITY_LOAD
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_ENTITY_SAVE
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_ENTITY_SPAWN
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_RELEASED_EVENT_PRE
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_SENT_POST
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_SENT_PRE
import farm.rosehearth.compatemon.events.CompatemonEvents.POKEMON_SENT_N_SPAWNED
import farm.rosehearth.compatemon.modules.pehkui.IScalableFormData
import farm.rosehearth.compatemon.modules.pehkui.util.CompatemonScaleUtils.Companion.setScale
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
            if(Compatemon.ShouldLoadMod(MOD_ID_APOTHEOSIS)){
                var isBoss = event.pokemonEntity.pokemon.persistentData.getCompound(MOD_ID_COMPATEMON).contains(APOTH_BOSS);
                if(isBoss){
                    var rarityKey = event.pokemonEntity.pokemon.persistentData.getCompound(MOD_ID_COMPATEMON).getString(APOTH_RARITY_COLOR)
                    event.pokemonEntity.pokemon.nickname = event.pokemonEntity.pokemon.nickname?.withStyle(Style.EMPTY.withColor(TextColor.parseColor(rarityKey)))
                }
            }
        }

        POKEMON_ENTITY_LOAD.subscribe{ event ->
            if(Compatemon.ShouldLoadMod(MOD_ID_PEHKUI)) {
                var scale = setScale(event.pokemonEntity, COMPAT_SCALE_SIZE)
               ( event.pokemonEntity.pokemon.form as IScalableFormData).`compatemon$setSizeScale`(scale)
            }
        }

        POKEMON_ENTITY_SPAWN.subscribe{event ->
            if(Compatemon.ShouldLoadMod(MOD_ID_PEHKUI)) {
                Compatemon.LOGGER.info("POKEMON_ENTITY_SPAWN FIRED: {}", event.entity.pokemon.species.name )

                var scale = setScale(event.entity, COMPAT_SCALE_SIZE)
                (event.entity.pokemon.form as IScalableFormData).`compatemon$setSizeScale`(scale)
                Compatemon.LOGGER.info("WE SET THE SPAWNED SCALE TO : {}", scale )

            }

        }

        POKEMON_SENT_PRE.subscribe{event ->
            Compatemon.LOGGER.info("POKEMON_SENT_PRE  FIRED: {}", event.pokemon.species.name )
        }
        POKEMON_SENT_POST.subscribe{event ->
            Compatemon.LOGGER.info("POKEMON_SENT_POST FIRED: {}", event.pokemon.species.name )
        }

        POKEMON_SENT_N_SPAWNED.subscribe { event ->
            var scale = 1.0f
            Compatemon.LOGGER.info("POKEMON_SENT_N_SPAWNED FIRED: {}", event.pokemon.species.name )
            Compatemon.LOGGER.info("POKEMON_SENT_N_SPAWNED Entity: {}", event.pokemonEntity?.pokemon?.species?.name )

            scale = setScale(event.pokemon.entity as Entity, COMPAT_SCALE_SIZE)
            (event.pokemonEntity?.form as IScalableFormData).`compatemon$setSizeScale`(scale)
            (event.pokemonEntity.pokemon.form as IScalableFormData).`compatemon$setSizeScale`(scale)
            (event.pokemon.form as IScalableFormData).`compatemon$setSizeScale`(scale)

            Compatemon.LOGGER.info("Sent out with a scale of : {}", scale )

        }
    }
}