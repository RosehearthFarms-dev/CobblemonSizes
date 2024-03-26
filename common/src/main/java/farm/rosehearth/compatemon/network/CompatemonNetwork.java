package farm.rosehearth.compatemon.network;

import commonnetwork.api.Network;
public class CompatemonNetwork {
	
	public CompatemonNetwork init(){
		Network
				.registerPacket(CompatemonPacketTest.CHANNEL, CompatemonPacketTest.class, CompatemonPacketTest::encode,CompatemonPacketTest::decode, CompatemonPacketTest::handle);
		
		return this;
	}
}
