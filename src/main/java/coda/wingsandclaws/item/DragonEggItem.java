package coda.wingsandclaws.item;

import coda.wingsandclaws.init.WingsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import coda.wingsandclaws.WingsAndClaws;

import net.minecraft.item.Item.Properties;

public class DragonEggItem extends Item {
    public DragonEggItem() {
        this(new Item.Properties().tab(WingsItems.GROUP));
    }

    public DragonEggItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();

        if (player != null) {
            ItemStack itemstack = player.getItemInHand(context.getHand());

            World world = context.getLevel();
            if (!world.isClientSide) {
                Entity entity = createEgg(itemstack, context.getClickedPos(), context.getClickedFace(), world, player);
                if (entity == null) return super.useOn(context);
                if (!player.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                world.addFreshEntity(entity);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            return ActionResultType.SUCCESS;
        }

        return super.useOn(context);
    }

    protected Entity createEgg(ItemStack stack, BlockPos pos, Direction direction, World world, PlayerEntity player) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();
            assert nbt != null;
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(WingsAndClaws.MOD_ID, nbt.getString("type")));
            if (type != null) {
                /*ProjectileItemEntity entity = new DragonEggEntity(type, world, player);
                entity.setItem(stack);
                entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
                world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                return entity;*/
            }
        }
        return null;
    }
}
