package net.msrandom.wings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.World;
import net.msrandom.wings.entity.item.MimangoEggEntity;

public class MimangoEggItem extends DragonEggItem {
    public MimangoEggItem() {
        super(new Item.Properties().group(WingsItems.GROUP));
    }

    @Override
    protected Entity createEgg(ItemStack stack, World world, PlayerEntity player) {
        if(world.getBlockState(player.getPosition().down()).getBlock().isIn(BlockTags.LEAVES))
            return new MimangoEggEntity(world, player);

        return null;
    }
}
