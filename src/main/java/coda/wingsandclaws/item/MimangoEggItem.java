package coda.wingsandclaws.item;

import coda.wingsandclaws.entity.item.MimangoEggEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MimangoEggItem extends DragonEggItem {
    public MimangoEggItem() {
        super(new Item.Properties().group(WingsItems.GROUP));
    }

    @Override
    protected Entity createEgg(ItemStack stack, BlockPos pos, Direction direction, World world, PlayerEntity player) {
        if (MimangoEggEntity.getEgg(world, pos) == null && world.getBlockState(pos).getBlock().isIn(BlockTags.LEAVES))
            return new MimangoEggEntity(world, pos);

        return null;
    }
}
