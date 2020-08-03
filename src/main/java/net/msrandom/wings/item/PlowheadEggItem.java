package net.msrandom.wings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.msrandom.wings.entity.item.PlowheadEggEntity;

public class PlowheadEggItem extends DragonEggItem {
    public PlowheadEggItem() {
        super(new Settings().group(WingsItems.GROUP));
    }

    @Override
    protected Entity createEgg(ItemStack stack, World world, PlayerEntity player) {
        return new PlowheadEggEntity(world, player);
    }
}
