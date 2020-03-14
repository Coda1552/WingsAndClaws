package random.wings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import random.wings.WingsAndClaws;
import random.wings.entity.item.DragonEggEntity;

public class DragonEggItem extends Item {
    public DragonEggItem() {
        this(new Item.Properties().group(WingsItems.GROUP));
    }

    public DragonEggItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!playerIn.abilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        if (!worldIn.isRemote) {
            Entity entity = createEgg(itemstack, worldIn, playerIn);
            if (entity != null) worldIn.addEntity(entity);
        }

        playerIn.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    protected Entity createEgg(ItemStack stack, World world, PlayerEntity player) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();
            assert nbt != null;
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(WingsAndClaws.MOD_ID, nbt.getString("type")));
            if (type != null) {
                ProjectileItemEntity entity = new DragonEggEntity(type, world, player);
                entity.func_213884_b(stack);
                entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
                world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                return entity;
            }
        }
        return null;
    }
}
