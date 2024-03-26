package farm.rosehearth.compatemon.network;

import commonnetwork.api.Network;
import farm.rosehearth.compatemon.network.packet.PokemonSizePacket;

public class CompatemonNetwork {
	
	public CompatemonNetwork init(){
		Network
				.registerPacket(CompatemonPacketTest.CHANNEL, CompatemonPacketTest.class, CompatemonPacketTest::encode, CompatemonPacketTest::decode, CompatemonPacketTest::handle)
				.registerPacket(PokemonSizePacket.CHANNEL, PokemonSizePacket.class, PokemonSizePacket::encode, PokemonSizePacket::decode, PokemonSizePacket::handle);
		
		return this;
	}
}
