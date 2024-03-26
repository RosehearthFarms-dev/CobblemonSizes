package farm.rosehearth.compatemon;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import commonnetwork.api.Dispatcher;
import commonnetwork.api.Network;
import farm.rosehearth.compatemon.config.Configuration;
import farm.rosehearth.compatemon.modules.apotheosis.ApotheosisConfig;
import farm.rosehearth.compatemon.modules.compatemon.CompatemonConfig;
import farm.rosehearth.compatemon.modules.pehkui.PehkuiConfig;
import farm.rosehearth.compatemon.modules.quark.QuarkConfig;
import farm.rosehearth.compatemon.modules.sophisticatedcore.SophisticatedConfig;
import farm.rosehearth.compatemon.network.CompatemonNetwork;
import farm.rosehearth.compatemon.network.CompatemonPacketTest;
import farm.rosehearth.compatemon.network.client.CompatemonNetworkClient;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import static farm.rosehearth.compatemon.util.CompatemonDataKeys.*;


public class Compatemon {
	public static final String MODID = "compatemon";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	
	public static CompatemonImplementation implementation;
	public static CompatemonNetwork network;
	public static CompatemonNetworkClient networkClient;
	public static File configDir;
	public static Configuration config;
	//public static final Map<String,Configuration> configs = new HashMap<>();
	private static final ArrayList<String> modsToConfigure = new ArrayList<String>();
	private static final Map<String, Boolean> modsToEnable = new HashMap<String, Boolean>();
	
	/**
	 * Add the MOD_IDs for each mod we add compatability for.
	 * Start with Fabric+Forge mods, then add modAPI specific ones after
	 */
	static {
		// Common
		modsToConfigure.add(MOD_ID_COMPATEMON);
		modsToConfigure.add(MOD_ID_PEHKUI);
		
		// Forge
		modsToConfigure.add(MOD_ID_APOTHEOSIS);
		//modsToConfigure.add(MOD_ID_QUARK);
		modsToConfigure.add(MOD_ID_SOPHISTICATEDSTORAGE);
		// Fabric
		// Quilt
		
	}
	
	/**
	 * Links the various modAPI implementations to the main mod. Allows for differing code.
	 * @param imp
	 */
	public static void preInitialize(CompatemonImplementation imp){
		implementation = imp;
		
		// Create the config file for Compatemon
		configDir = new File("config",MODID);
		config = new Configuration(new File(configDir, "MODULES.cfg"));
		config.setTitle("Compatemon Module Control");
		config.setComment("This file allows individual modules to be enabled or disabled. If the mod isn't loaded, it will automatically be set to false, and so long as it remains unloaded, will not do anything even if it's set to true.");
		
	}
	
	/**
	 * Initializes Common code between modAPIs
	 */
	public static void init() {
		loadConfigs(true);
		CompatemonKotlin.INSTANCE.initialize();
		Compatemon.implementation.postCommonInitialization();
		Compatemon.implementation.registerEvents();
		initNetwork();
	}
	
	/**
	 * Loads the configs for each mod. Config files are located in the common group, even if a particular mod is
	 * specific to a particular modAPI
	 *
	 */
	public static void loadConfigs(boolean initialB){
		
		// Adds each mod to the Compatemon config file
		for (String modID: modsToConfigure){
			
			boolean bEnabled = config.getBoolean("Enable " + modID.toUpperCase() + " Module", "general", implementation.isModInstalled(modID), "Enables the " + modID.toUpperCase() + " Module");
			modsToEnable.put(modID,implementation.isModInstalled(modID) && bEnabled);
			
		}
		
		if(config.hasChanged()) config.save();
		modsToEnable.put(MOD_ID_SOPHISTICATEDCORE,implementation.isModInstalled(MOD_ID_SOPHISTICATEDCORE));
		
		
		for(var m : modsToConfigure)
		{
			if(implementation.isModInstalled(m)) {
				Configuration c = new Configuration(new File(configDir, m + ".cfg"));
				
				switch(m){
					case MOD_ID_COMPATEMON:
						CompatemonConfig.load(c);
						break;
					case MOD_ID_APOTHEOSIS:
						ApotheosisConfig.load(c);
						break;
//					case MOD_ID_QUARK:
//						QuarkConfig.load(c);
//						break;
					case MOD_ID_PEHKUI:
						PehkuiConfig.load(c);
						break;
					case MOD_ID_SOPHISTICATEDSTORAGE:
						SophisticatedConfig.load(c);
						break;
				}
				if(!initialB && c.hasChanged()) c.save();
			}
		}
		
		
	}
	
	public static boolean ShouldLoadMod(String mod_id){     return modsToEnable.get(mod_id);    }
	
	
	
	
	/**
	 * Initialize the Compatemon Network - uses CommonNetworking Mod
	 */
	public static void initNetwork(){
		network = new CompatemonNetwork().init();
	}
	
	public static void onPlayerJoinServer(ServerPlayer player){
		Dispatcher.sendToClient(new CompatemonPacketTest(), player);
		Network.getNetworkHandler().sendToClient(new CompatemonPacketTest(), player);
	}
	
}