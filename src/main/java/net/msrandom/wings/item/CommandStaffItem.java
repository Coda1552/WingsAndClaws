package net.msrandom.wings.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

public class CommandStaffItem extends Item {
    public CommandStaffItem() {
        super(new Item.Properties().group(WingsItems.GROUP).maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        RayTraceResult mop = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
        if (mop.getType() != RayTraceResult.Type.MISS) {
            boolean flag = false;
            if (mop.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult result = ((EntityRayTraceResult) mop);
                flag = result.getEntity() instanceof TameableEntity && !((TameableEntity) result.getEntity()).isOwner(playerIn) && worldIn.getBlockState(result.getEntity().getPosition()).getMaterial() == Material.WATER;
            }
            else {
                BlockPos pos = ((BlockRayTraceResult) mop).getPos();
                for (Direction value : Direction.values()) {
                    flag = worldIn.getBlockState(pos.offset(value)).getMaterial() == Material.WATER;
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
                if (last != null) {
                    ItemStack stack = playerIn.getHeldItem(handIn);
                    last.setTarget(mop);
                    last.setStaff(stack);
                    return ActionResult.resultSuccess(stack);
                }
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}