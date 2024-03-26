package farm.rosehearth.compatemon.mixin.apotheosis;

import com.cobblemon.mod.common.api.spawning.context.SpawningContext;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import farm.rosehearth.compatemon.Compatemon;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = PokemonSpawnAction.class, remap = false)
abstract class MixinPokemonSpawningBoss extends SpawnAction<PokemonEntity> {
	
	public MixinPokemonSpawningBoss(@NotNull SpawningContext ctx, @NotNull SpawnDetail detail) {
		super(ctx, detail);
	}
	
	@Inject(method = "createEntity()Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;"
			,at = @At("RETURN"))
	private void compatemon$onSpawnRunFinalizeSpawn(CallbackInfoReturnable<PokemonEntity> cir) {
		if(cir.getReturnValue().getType().toString().equals("entity.cobblemon.pokemon")){
			var world = this.getCtx().getWorld();
			var pos = this.getCtx().getPosition();
			MobSpawnEvent.FinalizeSpawn newEvent = new MobSpawnEvent.FinalizeSpawn(cir.getReturnValue(), world, pos.getX(), pos.getY(), pos.getZ(), world.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null, null);
			MinecraftForge.EVENT_BUS.post(newEvent);
		}
		else{
			Compatemon.LOGGER.debug("The entity created by the POKEMON Spawn Action Class wasn't actually a pokemon?");
		}
	}
}
