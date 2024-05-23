package farm.rosehearth.compatemon


import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_ENTITY_SAVE
import farm.rosehearth.compatemon.util.CompatemonDataKeys.APOTH_BOSS
import farm.rosehearth.compatemon.util.CompatemonDataKeys.APOTH_RARITY_COLOR
import farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_APOTHEOSIS
import farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_COMPATEMON
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor

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

    }
}