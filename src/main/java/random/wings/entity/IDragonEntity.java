package random.wings.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import random.wings.item.WingsItems;

public interface IDragonEntity {
    default ItemStack getEgg() {
        ResourceLocation key = ((Entity) this).getType().getRegistryName();
        if (key != null) {
            ItemStack stack = new ItemStack(WingsItems.DRAGON_EGG);
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putString("type", key.getPath());
            return stack;
        }
        return ItemStack.EMPTY;
    }

    default void createEgg() {
        if (canBreed() && this instanceof AnimalEntity) {
            ((AnimalEntity) this).entityDropItem(getEgg());
        }
    }

    boolean canBreed();
}
