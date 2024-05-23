package farm.rosehearth.compatemon.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import farm.rosehearth.compatemon.Compatemon;
import farm.rosehearth.compatemon.modules.pehkui.util.CompatemonScaleUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static farm.rosehearth.compatemon.util.CompatemonDataKeys.*;

/**
 * Mixin to the Pokemon Entity Class. Using for Pehkui sizing.
 */
@Mixin(PokemonEntity.class)
abstract class MixinPokemonEntity extends Entity
{
    @Shadow(remap=false)
    private Pokemon pokemon;
    
    MixinPokemonEntity(EntityType<?> entityType, Level level){
        super(entityType, level);
    }
    
    @Inject(method = "<init>"
            ,at = @At("RETURN")
            ,remap = false)
    public void compatemon$onInit(Level world, Pokemon pokemon, EntityType entityType, CallbackInfo cir){
        if(Compatemon.ShouldLoadMod(MOD_ID_PEHKUI) && !pokemon.isClient$common()){
           CompatemonScaleUtils.Companion.setScale(((PokemonEntity) ((Object) this)), COMPAT_SCALE_SIZE);
        }
    }


    @Inject(at = @At("TAIL")
            ,method="setCustomName"
            ,remap=false)
    public void compatemon$injectSetCustomNameReturn(Component t, CallbackInfo cir){
        if(pokemon.getPersistentData().getCompound(MOD_ID_COMPATEMON).contains(APOTH_RARITY_COLOR)){
            pokemon.setNickname(t.copy().withStyle(Style.EMPTY.withColor(TextColor.parseColor(pokemon.getPersistentData().getCompound(MOD_ID_COMPATEMON).getString(APOTH_RARITY_COLOR)))));
        }
        
    }

}