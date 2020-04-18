package random.wings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import random.wings.entity.item.PlowheadEggEntity;

public class PlowheadEggItem extends DragonEggItem {
    public PlowheadEggItem() {
        super(new Item.Properties().group(WingsItems.GROUP));
    }

    @Override
    protected Entity createEgg(ItemStack stack, World world, PlayerEntity player) {
        return new PlowheadEggEntity(world, player);
    }
}
