package random.wings.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import random.wings.entity.monster.IcyPlowheadEntity;

public class CommandStaffItem extends Item {
    public CommandStaffItem() {
        super(new Item.Properties().group(WingsItems.GROUP));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        RayTraceResult mop = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
        if (mop.getType() != RayTraceResult.Type.MISS && worldIn.getBlockState(mop.getType() == RayTraceResult.Type.ENTITY ? ((EntityRayTraceResult) mop).getEntity().getPosition() : ((BlockRayTraceResult) mop).getPos().up()).getMaterial() == Material.WATER) {
            IcyPlowheadEntity last = null;
            for (IcyPlowheadEntity entity : worldIn.getEntitiesWithinAABB(IcyPlowheadEntity.class, playerIn.getBoundingBox().grow(64))) {
                if (entity.isOwner(playerIn) && (last == null || last.getDistanceSq(playerIn) > entity.getDistanceSq(playerIn))) {
                    last = entity;
                }
            }
            if (last != null) {
                last.setTarget(mop);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
