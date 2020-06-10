package net.msrandom.wings.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

import java.util.Collections;

public class CommandStaffItem extends ToolItem {
    public CommandStaffItem() {
        super(-2, -3, ItemTier.IRON, Collections.emptySet(), new Item.Properties().group(WingsItems.GROUP).maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        RayTraceResult mop = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
        if (mop.getType() != RayTraceResult.Type.MISS) {
            boolean flag = false;
            if (mop.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult result = ((EntityRayTraceResult) mop);
                flag = result.getEntity() instanceof TameableEntity && !((TameableEntity) result.getEntity()).isOwner(playerIn) && worldIn.getFluidState(result.getEntity().getPosition()).getFluid() == Fluids.WATER;
            }
            else {
                BlockPos pos = ((BlockRayTraceResult) mop).getPos();
                for (Direction value : Direction.values()) {
                    flag = worldIn.getFluidState(pos.offset(value)).getFluid() == Fluids.WATER;
                    if (flag) break;
                }
            }
            if (flag) {
                IcyPlowheadEntity last = null;
                for (IcyPlowheadEntity entity : worldIn.getEntitiesWithinAABB(IcyPlowheadEntity.class, playerIn.getBoundingBox().grow(64))) {
                    if (entity.isOwner(playerIn) && (last == null || last.getDistanceSq(playerIn) > entity.getDistanceSq(playerIn))) {
                        last = entity;
                    }
                }
                if (last != null && last.getState() != TameableDragonEntity.WonderState.STAY) {
                    ItemStack stack = playerIn.getHeldItem(handIn);
                    last.setTarget(mop);
                    last.setStaff(stack);
                    return ActionResult.resultSuccess(stack);
                }
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return HashMultimap.create();
    }
}
