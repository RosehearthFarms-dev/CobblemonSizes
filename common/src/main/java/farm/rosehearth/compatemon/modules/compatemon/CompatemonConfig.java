package farm.rosehearth.compatemon.modules.compatemon;

import farm.rosehearth.compatemon.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CompatemonConfig {
	public static final Logger LOGGER = LogManager.getLogger("Compatemon : Compatemon");
	
	public static int NicknameFieldLength = 64;
	
	/**
	 *
	 * @param c Configuration File to load these settings into
	 */
	public static void load(Configuration c){
		c.setTitle("Compatemon Configuration");
		c.setComment("This file contains settings that are non-specific to any particular mod other than Cobblemon or Vanilla Minecraft.");
		
		NicknameFieldLength = c.getInt("Nickname length", "nickname", NicknameFieldLength, 12, 64, "Max Length of Pokemon Nicknames. Used both in the actual nickname of a pokemon as well as in the Summary screen to set a pokemon's nickname as the max field length.\nCobblemon's Default is 12.");
	}
}
