package net.msrandom.wings.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.msrandom.wings.client.renderer.tileentity.PlowheadHornRenderer;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

import java.util.Collections;

public class PlowheadHornItem extends ToolItem {
    public PlowheadHornItem() {
        super(-2, -3, ItemTier.IRON, Collections.emptySet(), new Item.Properties().group(WingsItems.GROUP).maxStackSize(1).setISTER(() -> PlowheadHornRenderer::new));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            RayTraceResult mop = rayTrace(worldIn, player, RayTraceContext.FluidMode.NONE);
            if (mop.getType() != RayTraceResult.Type.MISS) {
                boolean flag = false;
                if (mop.getType() == RayTraceResult.Type.ENTITY) {
                    EntityRayTraceResult result = ((EntityRayTraceResult) mop);
                    flag = result.getEntity() instanceof TameableEntity && !((TameableEntity) result.getEntity()).isOwner(player) && worldIn.getFluidState(result.getEntity().getPosition()).getFluid() == Fluids.WATER;
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
                    if (last != null && last.getState() != TameableDragonEntity.WonderState.STAY) {
                        last.setTarget(mop);
                        last.setStaff(stack);
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
