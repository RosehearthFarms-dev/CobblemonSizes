package farm.rosehearth.compatemon.modules.apotheosis;

import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Add method to ApothBoss to allow an Entity to be passed to the createBoss function.
 */
public interface IApothBossEntity {
	default Mob createPokeBoss(ServerLevelAccessor world, BlockPos pos, RandomSource random, float luck, @Nullable LootRarity rarity, Entity entityFromSpawn){
		return (Mob)entityFromSpawn;
	}
	
}
