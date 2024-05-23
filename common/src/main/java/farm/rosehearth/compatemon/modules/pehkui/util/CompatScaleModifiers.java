package farm.rosehearth.compatemon.modules.pehkui.util;

import farm.rosehearth.compatemon.Compatemon;
import net.minecraft.resources.ResourceLocation;
import virtuoel.pehkui.api.*;

/**
 * This is where Compatemon Scales will be stored
 */
class CompatScaleModifiers  {
    public static final ScaleModifier COBBLEMON_ATK_MULTIPIER = register("cobblemon_atk_multiplier", new TypedScaleModifier(() -> ScaleTypes.BASE));
    public static final ScaleModifier COBBLEMON_ATK_DIVISOR = register("cobblemon_atk_divisor", new TypedScaleModifier(() -> ScaleTypes.BASE));
    
    /**
     *
     * @param path
     * @param scaleModifier
     * @return
     */
    private static ScaleModifier  register(String path, ScaleModifier scaleModifier){
        return ScaleRegistries.register(
                ScaleRegistries.SCALE_MODIFIERS,new ResourceLocation(Compatemon.MODID,path),scaleModifier
        );
    };
}
