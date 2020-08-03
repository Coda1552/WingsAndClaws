package net.msrandom.wings.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface IDragonEntity {
    default ItemStack getEgg() {
        Identifier key = Registry.ENTITY_TYPE.getId(((Entity) this).getType());
        ItemStack stack = ItemStack.EMPTY;//ItemStack(WingsItems.DRAGON_EGG); removed the dragon egg here, remove this if no dragons need it
        //CompoundTag nbt = stack.getOrCreateTag();
        //nbt.putString("type", key.getPath());
        return stack;
    }

    default void createEgg() {
        if (canDragonBreed() && this instanceof AnimalEntity) {
            ((AnimalEntity) this).dropStack(getEgg());
        }
    }

    default boolean canDragonBreed() {
        return this instanceof AnimalEntity && ((AnimalEntity) this).canBreed();
    }
}
