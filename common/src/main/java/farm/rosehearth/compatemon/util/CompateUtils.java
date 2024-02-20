package farm.rosehearth.compatemon.util;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import farm.rosehearth.compatemon.Compatemon;
import farm.rosehearth.compatemon.modules.apotheosis.ApotheosisConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Random;

import static farm.rosehearth.compatemon.util.CompatemonDataKeys.*;

/**
 *
 */
public class CompateUtils {
	public static Random Rand = new Random();
	
	
	public static boolean isApothBoss(Mob entity){
		boolean isBoss = false;
		if (entity.getType().toString().equals("entity.cobblemon.pokemon")){
			isBoss = ((PokemonEntity)entity).getPokemon().getPersistentData().getCompound(MOD_ID_COMPATEMON).contains(APOTH_BOSS);
		}else{
			isBoss = getPersistentData(entity).contains(APOTH_BOSS);
		}
		return isBoss;
	}
	
	public static CompoundTag getPersistentData(Entity entity){
		return entity.saveWithoutId(new CompoundTag()).getCompound(Compatemon.implementation.persistentDataKey());
	}
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(Compatemon.MODID, path);
	}
	
}

