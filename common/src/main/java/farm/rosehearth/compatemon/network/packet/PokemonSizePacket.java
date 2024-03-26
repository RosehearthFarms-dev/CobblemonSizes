package farm.rosehearth.compatemon.network.packet;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

import static farm.rosehearth.compatemon.util.CompateUtils.decodePokemon;
import static farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_COMPATEMON;

public class PokemonSizePacket {
	public static final ResourceLocation CHANNEL = new ResourceLocation(MOD_ID_COMPATEMON, "PokemonSizePacket");
	
	public Pokemon pokemon;
	public float pokemonSize;
	public UUID pokemonUUID;
	public UUID storeID;
	
	public PokemonSizePacket(){
		pokemon = new Pokemon();
		storeID = UUID.randomUUID();
		pokemonUUID = pokemon.getUuid();
		pokemonSize = 1.0f;
	}
	
	public PokemonSizePacket(Pokemon packetPokemon, float size_scale)
	{
		pokemon = packetPokemon;
		storeID = pokemon.getStoreCoordinates().get().getStore().getUuid();
		pokemonUUID = pokemon.getUuid();
		pokemonSize = size_scale;
	}
	
	public static PokemonSizePacket decode(FriendlyByteBuf buf)
	{
		return new PokemonSizePacket(decodePokemon(buf.readUUID(),
				buf.readUUID())
				,buf.readFloat());
	}
	
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(storeID);
		buf.writeUUID(pokemonUUID);
		buf.writeFloat(pokemonSize);
	}
	
	public static void handle(PacketContext<PokemonSizePacket> ctx)
	{
		if (Side.CLIENT.equals(ctx.side()))
		{
			Minecraft.getInstance().player.sendSystemMessage(Component.literal("PokemonSizePacket with a size of " + ctx.message().pokemonSize));
			//ctx.message().
		}
		else
		{
			ctx.sender().sendSystemMessage(Component.literal("PokemonSizePacket received on the server"));
		}
	}
}
