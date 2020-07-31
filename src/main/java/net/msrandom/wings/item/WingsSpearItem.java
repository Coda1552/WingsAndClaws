package net.msrandom.wings.item;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.msrandom.wings.entity.item.SpearProjectileEntity;

public class WingsSpearItem extends TridentItem {

    // TODO Bind ISTER to the model
    // TODO Change Melee Damage (Apparently it may be hard-coded in the game)

    public WingsSpearItem(Item.Properties builder) {
        super(builder);
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                int j = EnchantmentHelper.getRiptideModifier(stack);
                if (j <= 0 || playerEntity.isWet()) {
                    if (!worldIn.isRemote) {
                        stack.damageItem(1, playerEntity, playerEntity1 -> {
                            playerEntity1.sendBreakAnimation(entityLiving.getActiveHand());
                        });
                        if (j == 0) {
                            SpearProjectileEntity spearProjectileEntity = new SpearProjectileEntity(worldIn, playerEntity, stack);
                            spearProjectileEntity.shoot(playerEntity, playerEntity.rotationPitch, playerEntity.rotationYaw, 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
                            if (playerEntity.abilities.isCreativeMode) {
                                spearProjectileEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }

                            worldIn.addEntity(spearProjectileEntity);
                            worldIn.playMovingSound((PlayerEntity) null, spearProjectileEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!playerEntity.abilities.isCreativeMode) {
                                playerEntity.inventory.deleteStack(stack);

                            }

                        }

                    }

                }

                playerEntity.addStat(Stats.ITEM_USED.get(this));
                if (j > 0) {
                    float f7 = playerEntity.rotationYaw;
                    float f = playerEntity.rotationPitch;
                    float f1 = -MathHelper.sin(f7 * ((float) Math.PI / 180F)) * MathHelper.cos(f * ((float) Math.PI / 180F));
                    float f2 = -MathHelper.sin(f * ((float) Math.PI / 180F));
                    float f3 = MathHelper.cos(f7 * ((float) Math.PI / 180F)) * MathHelper.cos(f * ((float) Math.PI / 180F));
                    float f4 = MathHelper.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                    float f5 = 3.0F * ((1.0F + (float) j) / 4.0F);
                    f1 = f1 * (f5 / f4);
                    f2 = f2 * (f5 / f4);
                    f3 = f3 * (f5 / f4);
                    playerEntity.addVelocity((double) f1, (double) f2, (double) f3);
                    playerEntity.startSpinAttack(20);
                    if (playerEntity.onGround) {
                        float f6 = 1.1999999F;
                        playerEntity.move(MoverType.SELF, new Vec3d(0.0D, (double) 1.1999999F, 0.0D));
                    }
                }

                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
                playerEntity.addStat(Stats.ITEM_USED.get(this));
                worldIn.playMovingSound((PlayerEntity) null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);

            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return ActionResult.resultFail(itemStack);
        } else if (EnchantmentHelper.getRiptideModifier(itemStack) > 0 && !playerIn.isWet()) {
            return ActionResult.resultFail(itemStack);
        } else {
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemStack);
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, livingEntity -> {
            livingEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double) state.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(2, entityLiving, livingEntity -> {
                livingEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
        }
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }
}


