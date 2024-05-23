package farm.rosehearth.compatemon.modules.pehkui;

import farm.rosehearth.compatemon.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class PehkuiConfig {
	
	public static final Logger LOGGER = LogManager.getLogger("Compatemon : Pehkui");
	
	
	public static boolean size_do_unprovided = true;
	public static float size_dev = .200f;
	public static float size_scale = 1.0f;
	public static float size_max_percentage = 1.75f;
	public static float size_min_percentage = 0.25f;
	public static boolean weight_do_unprovided = true;
	public static float weight_dev = 0.20f;
	public static float weight_scale = 1.0f;
	public static float weight_max_percentage = 1.5f;
	public static float weight_min_percentage = 0.25f;
	public static boolean size_do_atk_scaling = true;
	public static boolean weight_do_atk_scaling = true;
	public static boolean size_do_def_scaling = true;
	public static boolean weight_do_def_scaling = true;
	public static boolean size_do_spd_scaling = true;
	public static boolean weight_do_spd_scaling = true;
	public static boolean size_do_hp_scaling = true;
	public static boolean weight_do_hp_scaling = true;
	
	/**
	 *
	 * @param c Configuration File to load these settings into
	 */
	public static void load(Configuration c){
		c.setTitle("Compatemon - Pehkui Configuration");
		
		// size config
		size_do_unprovided = c.getBoolean("Pokemon Sizing", "size", size_do_unprovided,"Should Pokemon sizes be randomized?");
		size_dev = c.getFloat("Size Standard Deviation", "size", size_dev,0,1,"The standard deviation of unprovided pokemon sizes");
		size_scale = c.getFloat("Default Size Scale", "size", size_scale, 0.01f, 10.0f,"The average size of most pokemon. Change to modify the scaling across the board!");
		size_min_percentage = c.getFloat("Minimum Size", "size", size_min_percentage,0.01f,10.0f,"The min size percentage for unprovided pokemon sizes, where 1.0 is 100% of the size scale.");
		size_max_percentage = c.getFloat("Maximum Size", "size", size_max_percentage,0.01f,10.0f,"The max size percentage for unprovided pokemon sizes, where 1.0 is 100% of the size scale.");
		
		// weight config
//		weight_do_unprovided = c.getBoolean("Do Unprovided Weight Changes", "weight", weight_do_unprovided,"\nIf not provided in the species jsons, should Pokemon weights be randomized? - Currently does not work. All are randomized anyway.");
//		weight_dev = c.getFloat("Weight Standard Deviation", "weight", weight_dev,0,1,"The standard deviation of unprovided pokemon sizes");
//		weight_scale = c.getFloat("Default Weight Scale", "weight", weight_scale, 0.01f, 10.0f,"The average weight of most pokemon. Change to modify the scaling across the board!");
//		weight_min_percentage = c.getFloat("Minimum Weight", "weight", weight_min_percentage,0.01f,10.0f,"The min weight percentage for unprovided pokemon sizes, where 1.0 is 100% of the weight scale.");
//		weight_max_percentage = c.getFloat("Maximum Weight", "weight", weight_max_percentage,0.01f,10.0f,"The max weight percentage for unprovided pokemon sizes, where 1.0 is 100% of the weight scale.");
//
//		size_do_atk_scaling = c.getBoolean("Attack Size Scale","attribute scaling",size_do_atk_scaling,"Should a pokemon's atk and sp atk scale with size?");
//		weight_do_atk_scaling = c.getBoolean("Attack Weight Scale","attribute scaling",weight_do_atk_scaling,"Should a pokemon's atk and sp atk scale with weight?");
//		size_do_def_scaling = c.getBoolean("Defense Size Scale","attribute scaling",size_do_def_scaling,"Should a pokemon's def and sp def scale with size?");
//		weight_do_def_scaling = c.getBoolean("Defense Weight Scale","attribute scaling",weight_do_def_scaling,"Should a pokemon's def and sp def scale with weight?");
//		size_do_spd_scaling = c.getBoolean("Speed Size Scale","attribute scaling",size_do_spd_scaling,"Should a pokemon's spd scale with size?");
//		weight_do_spd_scaling = c.getBoolean("Speed Weight Scale","attribute scaling",weight_do_spd_scaling,"Should a pokemon's spd scale with weight?");
//		size_do_hp_scaling = c.getBoolean("HP Size Scale","attribute scaling",size_do_hp_scaling,"Should a pokemon's hp scale with size?");
//		weight_do_hp_scaling = c.getBoolean("HP Weight Scale","attribute scaling",weight_do_hp_scaling,"Should a pokemon's hp scale with weight?");
//
	}
}
