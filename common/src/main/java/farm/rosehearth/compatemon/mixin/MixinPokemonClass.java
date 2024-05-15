package farm.rosehearth.compatemon.mixin;

import com.cobblemon.mod.common.api.reactive.SettableObservable;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.PokemonUpdatePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;
import farm.rosehearth.compatemon.Compatemon;
import farm.rosehearth.compatemon.events.CompatemonEvents;
import farm.rosehearth.compatemon.events.cobblemon.PokemonSentOutAndSpawnedEvent;
import farm.rosehearth.compatemon.events.entity.PokemonJsonLoadedEvent;
import farm.rosehearth.compatemon.events.entity.PokemonJsonSavedEvent;
import farm.rosehearth.compatemon.events.entity.PokemonNbtLoadedEvent;
import farm.rosehearth.compatemon.events.entity.PokemonNbtSavedEvent;
import farm.rosehearth.compatemon.modules.compatemon.IPokemonExtensions;
import farm.rosehearth.compatemon.net.messages.client.PehkuiSizeUpdatePacket;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static farm.rosehearth.compatemon.util.CompatemonDataKeys.*;

/**
 * Mixin to the Pokemon class file in order to actually allow a nickname to be colored? Why was this not a thing? One of the network packet handlers casts to string instead of just using the MutableText
 * Also adds events to the saveTo and loadFrom NBT/JSON methods.
 */
@Mixin(Pokemon.class)
abstract class MixinPokemonClass implements IPokemonExtensions {

	@Unique
	private float compatemon$pokemonScale = 1.0f;
	@Unique
	private SettableObservable<CompoundTag> _persistentData;
	
	@Shadow(remap=false)
	private float scaleModifier;
	
	@Shadow(remap=false) public abstract <T > SimpleObservable registerObservable(SimpleObservable<T> observable, Function1<? super T, ? extends PokemonUpdatePacket> notifyPacket);
	
	
	@Inject(method = "<init>"
			,at = @At("RETURN")
			,remap = false)
	public void compatemon$onInit(CallbackInfo cir){
		_persistentData = new SettableObservable<CompoundTag>(persistentData);
//		Pokemon p = (Pokemon)(Object)(this);
//		Function0<Pokemon> x = new Function0<>(){
//			@Override
//			public Pokemon invoke(){
//				return p;
//			}
//		};
//		Function1<CompoundTag, PehkuiSizeUpdatePacket> y = new Function1<>(){
//			@Override
//			public PehkuiSizeUpdatePacket invoke(CompoundTag aFloat){
//				return new PehkuiSizeUpdatePacket(x, ((IPokemonExtensions)(Object)p).compatemon$getPersistentData());
//			}
//		};
//
//		_persistentData = registerObservable(new SimpleObservable<CompoundTag>(),y);
	}
	
	
	// ===============================================================
	// Injections for Sending Out
	// ===============================================================
	
	@Inject(at= @At(value = "RETURN")
	,remap=false
	,method="sendOut")
	public void compatemon$addSendOutStartedEvent(ServerLevel level, Vec3 position, Function1<? super PokemonEntity, Unit> mutation, CallbackInfoReturnable<PokemonEntity> cir){
		if(cir.getReturnValue() != null){
			CompatemonEvents.POKEMON_SENT_N_SPAWNED.postThen(new PokemonSentOutAndSpawnedEvent((Pokemon)(Object)this, cir.getReturnValue(), level, position), savedEvent -> null, savedEvent -> {
				Compatemon.LOGGER.debug("We've posted the new SentNSpawned Event for {}", ((Pokemon)(Object)this).getSpecies().getName());
				return null;
			});
		}
	}
	
	// ===============================================================
	// Injections for Nickname things
	// ===============================================================
	
	@Shadow(remap=false)
	private @Nullable MutableComponent nickname;
	@Shadow(remap=false)
	private CompoundTag persistentData;
	
	
	
	@Inject(at=@At("RETURN")
		,method="getDisplayName"
		,remap=false
		,cancellable = true)
	public void compatemon$getDisplayNameWithColor(CallbackInfoReturnable<Component> cir){
		if(persistentData.getCompound(MOD_ID_COMPATEMON).contains(APOTH_RARITY_COLOR)){
			var x = cir.getReturnValue().copy().setStyle(Style.EMPTY.withColor(TextColor.parseColor(persistentData.getCompound(MOD_ID_COMPATEMON).getString(APOTH_RARITY_COLOR))));
			cir.setReturnValue(x);
		}
	}
	
	@Inject(at=@At("HEAD")
		,method="setNickname"
		,remap=false)
	public void compatemon$setNicknameWithColor(@Nullable MutableComponent value, CallbackInfo cir){
		if(value != null && persistentData.getCompound(MOD_ID_COMPATEMON).contains(APOTH_RARITY_COLOR)){
			nickname = value.withStyle(Style.EMPTY.withColor(TextColor.parseColor(persistentData.getCompound(MOD_ID_COMPATEMON).getString(APOTH_RARITY_COLOR))));
			value=nickname;
		}
	}
	
	
	
	// ===============================================================
	// Add Events to Save and Load from NBT/JSON
	// ===============================================================
	
	@Inject(method = "saveToNBT", at = @At("RETURN"), remap = false)
	private void addNBTSavingEvent(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> pokemon) {
		CompatemonEvents.POKEMON_NBT_SAVED.postThen(new PokemonNbtSavedEvent((Pokemon)((Object)this), nbt), savedEvent -> null, savedEvent -> {
			return null;
		});
	}
	
	@Inject(method = "loadFromNBT", at = @At("RETURN"), remap = false)
	private void addNBTLoadingEvent(CompoundTag nbt, CallbackInfoReturnable<Pokemon> pokemon) {
		CompatemonEvents.POKEMON_NBT_LOADED.post(new PokemonNbtLoadedEvent[]{new PokemonNbtLoadedEvent(pokemon.getReturnValue(), nbt)}, loadedEvent -> {
			return null;
		});
	}
	
	@Inject(method = "saveToJSON", at = @At("RETURN"), remap = false)
	private void addJSONSavingEvent(JsonObject json, CallbackInfoReturnable<JsonObject> pokemon) {
		CompatemonEvents.POKEMON_JSON_SAVED.postThen(new PokemonJsonSavedEvent((Pokemon)((Object)this), json), savedEvent -> null, savedEvent -> {
			return null;
		});
	}
	
	@Inject(method = "loadFromJSON", at = @At("RETURN"), remap = false)
	private void addJSONLoadingEvent(JsonObject json, CallbackInfoReturnable<Pokemon> pokemon) {
		CompatemonEvents.POKEMON_JSON_LOADED.post(new PokemonJsonLoadedEvent[]{new PokemonJsonLoadedEvent(pokemon.getReturnValue(), json)}, loadedEvent -> {
			return null;
		});
	}
	
	@Override
	public CompoundTag compatemon$getPersistentData(){
		return _persistentData.get();
	}
	
	@Override
	public void compatemon$setPersistentData(CompoundTag v){
		//persistentData.merge(v);
		_persistentData.set(v);
	}
	
	/**
	 * @author = Gormottian
	 * @reason = Making PersistentData Sync between client and server
	 */
	@Overwrite(remap = false)
	public final @NotNull CompoundTag getPersistentData(){
		return _persistentData.get();
	}
}
