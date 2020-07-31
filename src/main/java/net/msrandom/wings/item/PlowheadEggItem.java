package net.msrandom.wings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.msrandom.wings.entity.item.PlowheadEggEntity;

public class PlowheadEggItem extends DragonEggItem {
    public PlowheadEggItem() {
        super(new Item.Properties().group(WingsItems.GROUP));
    }

    @Override
    protected Entity createEgg(ItemStack stack, BlockPos pos, Direction direction, World world, PlayerEntity player) {
        pos = pos.offset(direction);
        return new PlowheadEggEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }
}
