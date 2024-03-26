package farm.rosehearth.compatemon.mixin.apotheosis;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.pokemon.properties.UncatchableProperty;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.adventure.AdventureConfig;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.util.SupportingEntity;
import farm.rosehearth.compatemon.Compatemon;
import farm.rosehearth.compatemon.api.entity.PersistantDespawner;
import farm.rosehearth.compatemon.modules.apotheosis.ApotheosisConfig;
import farm.rosehearth.compatemon.modules.apotheosis.IApothBossEntity;
import farm.rosehearth.compatemon.modules.pehkui.util.CompatemonScaleUtils;
import farm.rosehearth.compatemon.util.CompateUtils;
import farm.rosehearth.compatemon.util.CompatemonDataKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import dev.shadowsoffire.apotheosis.adventure.boss.ApothBoss;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import static farm.rosehearth.compatemon.util.CompatemonDataKeys.*;

/**
 * Apotheosis Spawning Flow
 * 1. Source
 * 2. ApothBoss item = DynamicRegistry.getRandomItem -> Pulls from datapack data. Sets the entity type of the boss.
 * 3. item.createBoss -> Creates the boss from the entity type above.
 * 4. inside createBoss -> initBoss. Spawns the boss.
 *
 * For pokemon, had to wrap a custom method for createBoss to get boss spawning on the pokemon spawn to work.
 */
@Mixin(ApothBoss.class)
abstract class MixinApothBoss
implements IApothBossEntity
{
	@Mutable
	@Final
	@Shadow(remap = false)
	@Nullable
	protected SupportingEntity mount;
	
	@Mutable
	@Final
	@Shadow(remap = false)
	protected EntityType<?> entity;
	
	@Mutable
	@Final
	@Shadow(remap = false)
	protected @Nullable CompoundTag nbt;
	
	
	@Shadow(remap=false)
	abstract public void initBoss(RandomSource rand, Mob entity, float luck, @Nullable LootRarity rarity);
	
	/**
	 * This code Fires whenever a boss is created and a pokemon item is chosen from the random list of availables
	 */
	@Inject(at = @At("HEAD")
			, method = "createBoss(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;FLdev/shadowsoffire/apotheosis/adventure/loot/LootRarity;)Lnet/minecraft/world/entity/Mob;"
			, remap = false)
	public void compatemon$onCreateBossHead(ServerLevelAccessor world, BlockPos pos, RandomSource random, float luck, @Nullable LootRarity rarity, CallbackInfoReturnable<Mob> boss) {
		if(Compatemon.ShouldLoadMod(MOD_ID_APOTHEOSIS) && Apotheosis.enableAdventure){
			var entityID = EntityType.getKey(this.entity).toString();
			
			if(entityID.equals("cobblemon:pokemon")){
				//TODO: PokemonPropertyGenerator with logic for Player Location and/or Spawning Biome + Player Stats
				//TODO: CustomProperty for Aggressive to work with FightOrFlight? Glowing needs to be longer and stay after reload
				String catchable = ApotheosisConfig.SpawnerBossesCatchable ? "" : " uncatchable";
				var properties = PokemonProperties.Companion.parse("species=random level=50" + catchable, " ", "=");
				properties.setLevel(random.nextInt(50) + 25);
				if(properties.getSpecies() == null){
					boss = null;
				}
				var pokemonEntity = properties.createEntity(world.getLevel());
				pokemonEntity.setDespawner(new PersistantDespawner<PokemonEntity>()); // never let it despawn
				pokemonEntity.setPersistenceRequired();
				nbt = pokemonEntity.saveWithoutId(new CompoundTag()); //writeNBT
				
				// if it's a pokemon, recalc health and hitbox and stuff
				
			}
		}
	}
	
	/**
	 * This code mostly fires when using the apoth spawn_boss command and when run from a rogue spawner
	 */
	@Inject(at = @At("RETURN")
			, method = "createBoss(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;F)Lnet/minecraft/world/entity/Mob;"
			, remap = false)
	public void compatemon$onCreateBossReturn(ServerLevelAccessor world, BlockPos pos, RandomSource random, float luck, CallbackInfoReturnable<Mob> boss) {
		
		if(Compatemon.ShouldLoadMod(MOD_ID_APOTHEOSIS) && Apotheosis.enableAdventure){
			var entityID = EntityType.getKey(this.entity).toString();
			
			if(entityID.equals("cobblemon:pokemon")){
				// Sets health to max and adds the glowing effect to command-spawned and spawner-spawned bosses
				boss.getReturnValue().setHealth(boss.getReturnValue().getMaxHealth());
				if (AdventureConfig.bossGlowOnSpawn) boss.getReturnValue().addEffect(new MobEffectInstance(MobEffects.GLOWING, 7200));
				
			}
		}
		
	}
	
	@Redirect(method="initBoss",remap=false
	,at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/Mob;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
	private boolean compatemon$removeGlowingFromPokemon(Mob boss, MobEffectInstance effect){
		if( boss.getType().toString().equals("entity.cobblemon.pokemon")){
			return false;
			
		}
		return boss.addEffect(effect);
	}
	
	@Inject(at = @At("RETURN")
		, remap = false
		, method = "initBoss")
	public void compatemon$initPokemonBossReturn(RandomSource rand, Mob entity, float luck, @Nullable LootRarity rarity, CallbackInfo cir) {
		
		if(Compatemon.ShouldLoadMod(MOD_ID_APOTHEOSIS) && entity.getType().toString().equals("entity.cobblemon.pokemon") && ApotheosisConfig.BossPokemonSpawnRate > 0 && Apotheosis.enableAdventure){
			CompoundTag compatemonData = new CompoundTag();
			compatemonData.put(CompatemonDataKeys.MOD_ID_COMPATEMON, new CompoundTag());
			compatemonData.getCompound(CompatemonDataKeys.MOD_ID_COMPATEMON).putBoolean(APOTH_BOSS, true);
			compatemonData.getCompound(CompatemonDataKeys.MOD_ID_COMPATEMON).putInt(APOTH_RARITY, rarity.ordinal());
			compatemonData.getCompound(CompatemonDataKeys.MOD_ID_COMPATEMON).putString(APOTH_RARITY_COLOR, rarity.getColor().serialize());
			
			((PokemonEntity)entity).getPokemon().getPersistentData().merge(compatemonData);
			((PokemonEntity)entity).getPokemon().setNickname(((PokemonEntity)entity).getPokemon().getNickname().withStyle(Style.EMPTY.withColor(rarity.getColor())));
		}
		
		if(Compatemon.ShouldLoadMod(MOD_ID_APOTHEOSIS) && Compatemon.ShouldLoadMod(MOD_ID_PEHKUI) && CompateUtils.isApothBoss(entity) && Apotheosis.enableAdventure){
			
			var addToScale = 0.0f;
			var isBoss = true;
			switch(ApotheosisConfig.BossSizingEntities.toUpperCase()){
				case "POKEMON":
					if(!(entity instanceof PokemonEntity))
						isBoss = false;
					break;
				case "NON-POKEMON":
					if((entity instanceof PokemonEntity))
						isBoss = false;
					break;
				case "ALL":
					break;
				default:
					isBoss=false;
					break;
			}
			if(isBoss){
				addToScale = 0.0f;
			}
			CompatemonScaleUtils.Companion.setScale(entity, COMPAT_SCALE_SIZE,  isBoss);
			
			
		}
		
	}
	
//	@Inject(
//			at=@At("HEAD")
//			,remap = false
//			,method="modifyBossItem")
//	private static void compatemon$modifyBossItemHEAD(ItemStack stack, RandomSource rand, String bossName, float luck, LootRarity rarity, BossStats stats, CallbackInfoReturnable<ItemStack> item)
//	{
//	}
	
	@Shadow(remap=false)
	public abstract Mob createBoss(ServerLevelAccessor world, BlockPos pos, RandomSource random, float luck, @Nullable LootRarity rarity);
	
	@Override
	public Mob createPokeBoss(ServerLevelAccessor world, BlockPos pos, RandomSource random, float luck, @Nullable LootRarity rarity, Entity entityFromSpawn) {
		if(Compatemon.ShouldLoadMod(MOD_ID_APOTHEOSIS) && entityFromSpawn.getType().toString().equals("entity.cobblemon.pokemon" ) && ApotheosisConfig.BossPokemonSpawnRate > 0 && Apotheosis.enableAdventure){
			
			entity = entityFromSpawn.getType();
			
			int clevel = ((PokemonEntity) entityFromSpawn).getPokemon().getLevel(); // spawned pokemon level - based on levels of pokemon in player's party
			int range = ApotheosisConfig.PokemonBossLevelRange;
			int levelVariance = ApotheosisConfig.LevelVariancePerRarity * (rarity != null ? rarity.ordinal() : 0);
			int newLevel = clevel + random.nextInt(range) - (range / 2) + levelVariance;
			((PokemonEntity) entityFromSpawn).getPokemon().setLevel(newLevel);
			// Kind of expect this to break sometime if the level is out of the range. Hoping Cobblemon accounted for that though.
			
			if(!ApotheosisConfig.BossPokemonCatchable){
				((PokemonEntity) entityFromSpawn).getPokemon().getCustomProperties().add(UncatchableProperty.INSTANCE.uncatchable());
			}
			
			((PokemonEntity) entityFromSpawn).setDespawner(new PersistantDespawner<PokemonEntity>()); // never let it despawn
			nbt = ((PokemonEntity) entityFromSpawn).saveWithoutId(new CompoundTag());
			
			CompoundTag fakeNbt = this.nbt;
			fakeNbt.putString("id", EntityType.getKey(this.entity).toString());
			
			Mob entity = (Mob) entityFromSpawn;
			if(this.nbt != null) entity.load(this.nbt);
			
			this.initBoss(random, entity, luck, rarity);
			
			((PokemonEntity) entity).getPokemon().setLevel(newLevel);
			// had to take out the mount info as for SOME reason, it kept trying to create spiders
			// would LOVe to add in bosses mounted as pokemon at some point as compatability with Cobblemounts
			
			entity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
			return entity;
		}
		else{
			Compatemon.LOGGER.debug("Using the default createBoss");
			return createBoss(world,pos,random,luck,rarity);
		}
	}
}
