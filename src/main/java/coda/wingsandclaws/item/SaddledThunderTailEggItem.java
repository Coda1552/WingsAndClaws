package coda.wingsandclaws.item;

import coda.wingsandclaws.entity.item.SaddledThunderTailEggEntity;
import coda.wingsandclaws.init.WingsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SaddledThunderTailEggItem extends DragonEggItem {
    public SaddledThunderTailEggItem() {
        super(new Item.Properties().group(WingsItems.GROUP));
    }

    @Override
    protected Entity createEgg(ItemStack stack, BlockPos pos, Direction direction, World world, PlayerEntity player) {
        pos = pos.offset(direction);
        return new SaddledThunderTailEggEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }
}
