package farm.rosehearth.compatemon.mixin.client;

import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import farm.rosehearth.compatemon.Compatemon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static farm.rosehearth.compatemon.util.CompatemonDataKeys.COMPAT_SCALE_SIZE;
import static farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_COMPATEMON;

/**
 * The idea is to make the render size match the bounding box? May still be having trouble with this.
 */
@Mixin(PokemonRenderer.class)
abstract class MixinPokemonRenderer {
//	@Unique
//	private float compatemon$newScale = 1.0f;
	
//	@Inject(at=@At("HEAD"),remap=false,method="scale(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V")
//	public void compatemon$onScale(PokemonEntity pEntity, PoseStack pMatrixStack, float pPartialTickTime, CallbackInfo cir){
//		if(pEntity.getPokemon().getPersistentData().getCompound(MOD_ID_COMPATEMON).contains(COMPAT_SCALE_SIZE))
//			compatemon$newScale = pEntity.getPokemon().getPersistentData().getCompound(MOD_ID_COMPATEMON).getFloat(COMPAT_SCALE_SIZE);
//	}
	
//	@Inject(at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V")
//	,remap=false, locals = LocalCapture.CAPTURE_FAILSOFT
//	,method="scale(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V")
//	private void compatemon$onScale(PokemonEntity pEntity, PoseStack pMatrixStack, float pPartialTickTime, CallbackInfo cir, float scale) {
//
//	}
	
//	@ModifyVariable(at = @At("LOAD"), name="scale"
//			, remap = false//, locals = LocalCapture.CAPTURE_FAILSOFT
//			, method = "scale(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V")
//	private float compatemon$modifyScale(float scale) {
//		Compatemon.LOGGER.debug("In the PokemonRenderer, setting the scale: " + scale);
//		Compatemon.LOGGER.debug("NewScale: " + (compatemon$newScale));
//		return scale ;//* compatemon$newScale;
//	}
}
