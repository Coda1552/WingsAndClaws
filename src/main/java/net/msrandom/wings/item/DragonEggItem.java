package net.msrandom.wings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.WingsAndClaws;

public class DragonEggItem extends Item {
    public DragonEggItem() {
        this(new Item.Properties().group(WingsItems.GROUP));
    }

    public DragonEggItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getStackInHand(hand);
        if (!playerIn.abilities.creativeMode) {
            itemstack.decrement(1);
        }

        if (!world.isClient) {
            Entity entity = createEgg(itemstack, world, playerIn);
            if (entity != null) world.spawnEntity(entity);
        }

        playerIn.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    protected Entity createEgg(ItemStack stack, World world, PlayerEntity player) {
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            assert nbt != null;
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new Identifier(WingsAndClaws.MOD_ID, nbt.getString("type")));
            if (type != null) {
                /*ThrownItemEntity entity = new DragonEggEntity(type, world, player);
                entity.func_213884_b(stack);
                entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                return entity;*/
            }
        }
        return null;
    }
}
