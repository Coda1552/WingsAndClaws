package coda.wingsandclaws.item;

import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import net.minecraft.item.Item.Properties;

public class WingsSpawnEggItem extends SpawnEggItem {
    private final Supplier<EntityType<?>> type;

    @SuppressWarnings("ConstantConditions")
    public WingsSpawnEggItem(Supplier<EntityType<?>> type, int primaryColor, int secondaryColor, Properties builder) {
        //Passing null here will just put a null key in the backing map, which will be overridden with each egg but that doesn't matter as we don't rely it
        super(null, primaryColor, secondaryColor, builder);
        this.type = type;
    }

    @Override
    public EntityType<?> getType(@Nullable CompoundNBT nbt) {
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundNBT compoundnbt = nbt.getCompound("EntityTag");
            if (compoundnbt.contains("id", 8)) {
                return EntityType.byString(compoundnbt.getString("id")).orElseGet(type);
            }
        }

        return type.get();
    }
}
