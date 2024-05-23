package farm.rosehearth.compatemon.mixin;

import com.cobblemon.mod.common.net.messages.PokemonDTO;
import com.cobblemon.mod.common.pokemon.Pokemon;
import farm.rosehearth.compatemon.modules.compatemon.IPokemonDTOExtensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for PokemonDTO class to add passing the pokemon's persistent data with the DTO object
 */
@Mixin(PokemonDTO.class)
abstract class MixinPokemonDTO
implements IPokemonDTOExtensions {
	
	@Unique
	private CompoundTag compatemon$persistentData = new CompoundTag();
	
	
	@Override
	public CompoundTag compatemon$getPersistentData(){
		return compatemon$persistentData;
	}
	@Override
	public void compatemon$setPersistentData(CompoundTag v){
		this.compatemon$persistentData = v;
	}
	
	
//	@Inject(at=@At("RETURN")
//	,remap=false
//	,method="<init>()V")
//	public void compatemon$injectIntoDefaultConstructor(CallbackInfo ci){ }
	
	@Inject(at=@At("RETURN")
			,remap=false
			,method="<init>(Lcom/cobblemon/mod/common/pokemon/Pokemon;Z)V")
	public void compatemon$injectIntoConstructor(Pokemon pokemon, boolean toClient, CallbackInfo ci){
		compatemon$persistentData = pokemon.getPersistentData();
	}
	
	@Inject(at=@At("RETURN")
	,remap=false
	,method="encode")
	public void compatemon$injectEncode(FriendlyByteBuf buffer, CallbackInfo ci){
		//Compatemon.LOGGER.debug("We're encoding the pokemon here!");
		buffer.writeNbt(compatemon$persistentData) ;
		//Compatemon.LOGGER.debug(compatemon$persistentData.toString());
	}
	
	@Inject(at=@At("RETURN")
			,remap=false
			,method="decode")
	public void compatemon$injectDecode(FriendlyByteBuf buffer, CallbackInfo ci){
		//Compatemon.LOGGER.debug("We're decoding the pokemon here!");
		//compatemon$persistentData = buffer.readNullable(v -> buffer.readNbt());
		compatemon$persistentData = buffer.readNbt();
		//Compatemon.LOGGER.debug(compatemon$persistentData.toString());
	}
	
	@Inject(at=@At("TAIL")
	,remap=false
	,method="create")
	public void compatemon$createPokemon$tail(CallbackInfoReturnable<Pokemon> cir){
//		Compatemon.LOGGER.debug("====================================================================");
//		Compatemon.LOGGER.debug("We're creating the pokemon here! Here's the entity's NBT Data: ");
		//Compatemon.LOGGER.info(compatemon$persistentData.toString());
		cir.getReturnValue().getPersistentData().merge(compatemon$persistentData);
		CompoundTag x = cir.getReturnValue().saveToNBT(new CompoundTag());
//		Compatemon.LOGGER.debug(x.toString());
//		Compatemon.LOGGER.debug("====================================================================");
	}
}
