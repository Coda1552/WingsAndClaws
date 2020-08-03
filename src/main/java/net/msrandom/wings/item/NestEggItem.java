package net.msrandom.wings.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.msrandom.wings.block.NestBlock;

public class NestEggItem extends Item {
    public NestEggItem(NestBlock<?> block) {
        super(new Item.Settings().group(WingsItems.GROUP));
        block.setItem(this);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, WorldView world, BlockPos pos, PlayerEntity player) {
        return true;
    }
}
