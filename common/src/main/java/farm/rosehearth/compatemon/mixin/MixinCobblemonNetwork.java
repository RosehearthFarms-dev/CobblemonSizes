package farm.rosehearth.compatemon.mixin;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler;
import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.common.client.net.pokemon.update.PokemonUpdatePacketHandler;
import farm.rosehearth.compatemon.Compatemon;
import farm.rosehearth.compatemon.net.messages.client.PehkuiSizeUpdatePacket;
import kotlin.jvm.functions.Function1;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_PEHKUI;

@Mixin(CobblemonNetwork.class)
abstract class MixinCobblemonNetwork {
	
	@Shadow(remap=false) protected abstract <T extends NetworkPacket<T>> void createClientBound(ResourceLocation par1, Function1<? super FriendlyByteBuf, ? extends T> par2, ClientNetworkPacketHandler<T> par3);
	
	@Inject(method = "<init>"
			,at = @At("RETURN")
			,remap = false)
	public void compatemon$onInit(CallbackInfo cir){
		if(Compatemon.ShouldLoadMod(MOD_ID_PEHKUI)){
		
		}
	}
	
	
	@Inject(method = "registerClientBound"
			,at = @At("RETURN")
			,remap = false)
	public void compatemon$injectRegisterClientBound(CallbackInfo cir){
		if(Compatemon.ShouldLoadMod(MOD_ID_PEHKUI)){
			createClientBound(PehkuiSizeUpdatePacket.Companion.getID(), PehkuiSizeUpdatePacket.Companion::decode, new PokemonUpdatePacketHandler());
		}
	}
	
	//@Inject(method= "createClientBound(Lnet/minecraft/resources/ResourceLocation;Lkotlin/jvm/functions/Function1;Lcom/cobblemon/mod/common/api/net/ClientNetworkPacketHandler;)V")
}
