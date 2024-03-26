package farm.rosehearth.compatemon.modules.compatemon;

import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.entity.EntityProperty;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;

public interface IPokemonEntityExtensions {
	
	abstract EntityDataAccessor<CompoundTag> getPERSISTENT_DATA();
	abstract void setPERSISTENT_DATA(EntityDataAccessor<CompoundTag> value);
	
	abstract EntityProperty<CompoundTag> compatemon$getPersistentData();
	abstract void compatemon$setPersistentData(EntityProperty<CompoundTag> value);
	
	abstract ObservableSubscription<?> getSubscriptions();
}
