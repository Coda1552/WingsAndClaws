package random.wings.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import random.wings.WingsAndClaws;
import random.wings.entity.DragonEggEntity;

public class DragonEggItem extends Item {
    public DragonEggItem() {
        super(new Item.Properties().group(WingsItems.GROUP));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!playerIn.abilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isRemote) {
            if (itemstack.hasTag()) {
                CompoundNBT nbt = itemstack.getTag();
                assert nbt != null;
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(WingsAndClaws.MOD_ID, nbt.getString("type")));
                if (type != null) {
                    DragonEggEntity entity = new DragonEggEntity(type, worldIn, playerIn);
                    entity.func_213884_b(itemstack);
                    entity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
                    worldIn.addEntity(entity);
                }
            }
        }

        playerIn.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
