package coda.wingsandclaws.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SpawnEggItem.class)
public interface SpawnEggsAccessor {
    @Accessor(value = "EGGS")
    static Map<EntityType<?>, SpawnEggItem> getEggs() {
        throw new IllegalStateException();
    }
}
