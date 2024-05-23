package farm.rosehearth.compatemon.events.apotheosis

import com.cobblemon.mod.common.api.events.Cancelable
import net.minecraft.world.entity.LivingEntity

data class ApothBossSpawnedEvent(val e: LivingEntity): Cancelable()
