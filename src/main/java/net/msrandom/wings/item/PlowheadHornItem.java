package net.msrandom.wings.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.client.renderer.tileentity.PlowheadHornRenderer;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

import java.util.Collections;

public class PlowheadHornItem extends ToolItem {
    public PlowheadHornItem() {
        super(-2, -3, ItemTier.IRON, Collections.emptySet(), new Item.Properties().group(WingsItems.GROUP).maxStackSize(1).setISTER(() -> PlowheadHornRenderer::new));
        this.addPropertyOverride(new ResourceLocation(WingsAndClaws.MOD_ID, "using"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            if (!stack.hasTag() || playerIn.ticksExisted - stack.getTag().getInt("LastUsage") > 48) {
                playerIn.setActiveHand(handIn);
                worldIn.playSound(null, playerIn.getPosition(), WingsSounds.BATTLE_HORN, SoundCategory.PLAYERS, 1, 1);
                if (!stack.hasTag()) stack.setTag(new CompoundNBT());
                stack.getTag().putInt("LastUsage", playerIn.ticksExisted);
                return ActionResult.resultSuccess(stack);
            }
        }
        return ActionResult.resultFail(stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            Vec3d vec = player.getPositionVec().add(player.getLookVec());
            EntityRayTraceResult entityTrace = worldIn.getEntitiesWithinAABB(TameableDragonEntity.class, new AxisAlignedBB(vec.x - 2, vec.y - 2, vec.z - 2, vec.x + 2, vec.y + 2, vec.z + 2)).stream().reduce((a, b) -> a.getDistanceSq(player) < b.getDistanceSq(player) ? a : b).map(EntityRayTraceResult::new).orElse(null);
            RayTraceResult mop = entityTrace == null || entityTrace.getType() == RayTraceResult.Type.MISS ? rayTrace(worldIn, player, RayTraceContext.FluidMode.NONE) : entityTrace;
            if (mop.getType() != RayTraceResult.Type.MISS) {
                boolean flag = false;
                if (mop.getType() == RayTraceResult.Type.ENTITY) {
                    EntityRayTraceResult result = ((EntityRayTraceResult) mop);
                    if (!((TameableDragonEntity) result.getEntity()).isOwner(player)) {
                        flag = worldIn.getFluidState(result.getEntity().getPosition()).getFluid() == Fluids.WATER;
                    } else {
                        TameableDragonEntity entity = (TameableDragonEntity) result.getEntity();
                        TameableDragonEntity.WonderState state = entity.getState();
                        TameableDragonEntity.WonderState newState = state == TameableDragonEntity.WonderState.FOLLOW ? TameableDragonEntity.WonderState.STAY : TameableDragonEntity.WonderState.values()[state.ordinal() + 1];
                        entity.setState(newState);
                        player.sendStatusMessage(new TranslationTextComponent("entity." + WingsAndClaws.MOD_ID + ".state." + newState.name().toLowerCase()), true);
                        return stack;
                    }
                } else {
                    BlockPos pos = ((BlockRayTraceResult) mop).getPos();
                    for (Direction value : Direction.values()) {
                        flag = worldIn.getFluidState(pos.offset(value)).getFluid() == Fluids.WATER;
                        if (flag) break;
                    }
                }
                if (flag) {
                    IcyPlowheadEntity last = null;
                    for (IcyPlowheadEntity entity : worldIn.getEntitiesWithinAABB(IcyPlowheadEntity.class, player.getBoundingBox().grow(64))) {
                        if (entity.isOwner(player) && (last == null || last.getDistanceSq(player) > entity.getDistanceSq(player))) {
                            last = entity;
                        }
                    }
                    if (last != null) {
                        if (last.getState() == TameableDragonEntity.WonderState.STAY)
                            last.setState(TameableDragonEntity.WonderState.WONDER);
                        last.setTarget(mop);
                        last.setHorn(stack);
                        return stack;
                    }
                }
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return HashMultimap.create();
    }
}
