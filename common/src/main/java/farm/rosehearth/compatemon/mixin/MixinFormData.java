package farm.rosehearth.compatemon.mixin;

import com.cobblemon.mod.common.pokemon.FormData;
import farm.rosehearth.compatemon.Compatemon;
import farm.rosehearth.compatemon.modules.pehkui.IScalableFormData;
import net.minecraft.world.entity.EntityDimensions;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FormData.class)
abstract class MixinFormData
implements IScalableFormData {
	
	@Unique
	public float compatemon$pokemonSizeScale = 1.0f;
	
//	@Inject(at=@At("RETURN"), remap=false, method="getHitbox", cancellable = true)
//	public void compatemon$onGetHitbox(CallbackInfoReturnable<EntityDimensions> cir){
//		//Compatemon.LOGGER.info("In getHitbox, settin the scale of the form to {}", compatemon$pokemonSizeScale);
//		//cir.setReturnValue(cir.getReturnValue().scale(compatemon$pokemonSizeScale));
//	}
	
	@Override
	public float compatemon$getSizeScale(){
		return compatemon$pokemonSizeScale;
	}
	
	@Override
	public void compatemon$setSizeScale(float sizeScale){
		compatemon$pokemonSizeScale = sizeScale;
	}
	
}
