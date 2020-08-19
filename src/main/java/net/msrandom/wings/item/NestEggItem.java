package net.msrandom.wings.item;

import net.minecraft.item.Item;
import net.msrandom.wings.block.NestBlock;

public class NestEggItem extends Item {
    public NestEggItem(NestBlock<?> block) {
        super(new Item.Settings().group(WingsItems.GROUP));
        block.setItem(this);
    }

/*    @Override
    public boolean doesSneakBypassUse(ItemStack stack, WorldView world, BlockPos pos, PlayerEntity player) {
        return true;
    }*/
}
