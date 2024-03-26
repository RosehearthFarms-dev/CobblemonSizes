package farm.rosehearth.compatemon.mixin;

import com.cobblemon.mod.common.entity.EntityProperty;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import farm.rosehearth.compatemon.Compatemon;
import farm.rosehearth.compatemon.modules.compatemon.IPokemonEntityExtensions;
import farm.rosehearth.compatemon.modules.pehkui.IScalablePokemonEntity;
import farm.rosehearth.compatemon.modules.pehkui.util.CompatemonScaleUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
    @Unique
    private float compatemon$sizeScale = 1.0f;
    
    MixinPokemonEntity(EntityType<?> entityType, Level level){
        super(entityType, level);
    }
    
    @Inject(method = "<init>"
            ,at = @At("RETURN")
            ,remap = false)
    public void compatemon$onInit(Level world, Pokemon pokemon, EntityType entityType, CallbackInfo cir){
        if(Compatemon.ShouldLoadMod(MOD_ID_PEHKUI) && !pokemon.isClient$common()){
            compatemon$sizeScale = CompatemonScaleUtils.Companion.setScale(((PokemonEntity) ((Object) this)), COMPAT_SCALE_SIZE);;
            Compatemon.LOGGER.debug("Size Scale is being generated in the init: {}", compatemon$sizeScale);
        }
    }
    
    @Inject(method="<clinit>"
    ,at=@At("RETURN")
    ,remap=false)
    private static void compatemon$onclinitReturn(CallbackInfo ci){
        Compatemon.LOGGER.debug("In clinit PokemonEntity");
        //PERSISTENT_DATA = SynchedEntityData.defineId(PokemonEntity.class, EntityDataSerializers.COMPOUND_TAG);
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