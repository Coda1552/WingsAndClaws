package net.msrandom.wings.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccessReader;
import net.msrandom.wings.block.NestBlock;

public class NestEggItem extends Item {
    public NestEggItem(NestBlock<?> block) {
        super(new Item.Properties().group(WingsItems.GROUP));
        block.setItem(this);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, ServerWorldAccessReader world, BlockPos pos, PlayerEntity player) {
        return true;
    }
}
