package farm.rosehearth.compatemon.network.client;

import commonnetwork.api.Dispatcher;
import commonnetwork.api.Network;
import farm.rosehearth.compatemon.network.CompatemonNetwork;
import farm.rosehearth.compatemon.network.CompatemonPacketTest;

public class CompatemonNetworkClient {
	public CompatemonNetworkClient(){
		new CompatemonNetwork().init();
	}
	
	public void onJoinWorld(){
		Dispatcher.sendToServer(new CompatemonPacketTest());
		Network.getNetworkHandler().sendToServer(new CompatemonPacketTest());
	}
}
