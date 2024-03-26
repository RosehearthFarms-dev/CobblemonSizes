package farm.rosehearth.compatemon.mixin.client;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import farm.rosehearth.compatemon.Compatemon;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import farm.rosehearth.compatemon.modules.compatemon.IPokemonEntityExtensions;
import farm.rosehearth.compatemon.modules.pehkui.util.CompatemonScaleUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static farm.rosehearth.compatemon.util.CompatemonDataKeys.COMPAT_SCALE_SIZE;
import static farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_PEHKUI;

@Mixin(PokemonClientDelegate.class)
abstract class MixinPokemonClientDelegate {

	@Shadow(remap=false)
	public PokemonEntity currentEntity;
	
	@Inject(at=@At("RETURN"), remap=false, method="changePokemon")
	public void compatemon$onChangePokemonFireEvent(Pokemon pokemon, CallbackInfo cir){
	}
	
	//@Inject(at=@At("RETURN"), remap=false, method="")
	//public void compatemon$methodName(CallbackInfoReturnable<> cir){}
	
}
