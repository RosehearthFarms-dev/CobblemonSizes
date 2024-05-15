package farm.rosehearth.compatemon.modules.compatemon;


import net.minecraft.nbt.CompoundTag;

public interface IPokemonExtensions {
	abstract CompoundTag compatemon$getPersistentData();
	abstract void compatemon$setPersistentData(CompoundTag v);
}
