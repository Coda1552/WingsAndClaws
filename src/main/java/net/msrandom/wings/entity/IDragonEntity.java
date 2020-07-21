package net.msrandom.wings.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public interface IDragonEntity {
    default ItemStack getEgg() {
        Identifier key = ((Entity) this).getType().getRegistryName();
        if (key != null) {
            ItemStack stack = ItemStack.EMPTY;//ItemStack(WingsItems.DRAGON_EGG); removed the dragon egg here, remove this if no dragons need it
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putString("type", key.getPath());
            return stack;
        }
        return ItemStack.EMPTY;
    }

    default void createEgg() {
        if (canDragonBreed() && this instanceof AnimalEntity) {
            ((AnimalEntity) this).dropItem(getEgg());
        }
    }

    default boolean canDragonBreed() {
        return this instanceof AnimalEntity && ((AnimalEntity) this).canBreed();
    }
}
