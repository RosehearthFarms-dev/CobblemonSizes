package farm.rosehearth.compatemon.network;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static farm.rosehearth.compatemon.util.CompatemonDataKeys.MOD_ID_COMPATEMON;

public class CompatemonPacketTest {
	public static final ResourceLocation CHANNEL = new ResourceLocation(MOD_ID_COMPATEMON, "example_packet_one");
	
	public CompatemonPacketTest()
	{
	}
	
	public static CompatemonPacketTest decode(FriendlyByteBuf buf)
	{
		return new CompatemonPacketTest();
	}
	
	public void encode(FriendlyByteBuf buf)
	{
	
	}
	
	public static void handle(PacketContext<CompatemonPacketTest> ctx)
	{
		if (Side.CLIENT.equals(ctx.side()))
		{
			Minecraft.getInstance().player.sendSystemMessage(Component.literal("CompatemonPacketTest on the client!"));
		}
		else
		{
			ctx.sender().sendSystemMessage(Component.literal("CompatemonPacketTest received on the server"));
		}
	}
}
