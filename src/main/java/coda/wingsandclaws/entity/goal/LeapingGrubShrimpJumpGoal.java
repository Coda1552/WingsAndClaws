/*
package coda.wingsandclaws.entity.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.JumpGoal;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import coda.wingsandclaws.entity.LeapingGrubShrimpEntity;

public class LeapingGrubShrimpJumpGoal  extends JumpGoal {
    private static final int[] JUMP_DISTANCES = new int[]{0, 1, 4, 5, 6, 7};
    private final LeapingGrubShrimpEntity entity;
    private final int chance;
    private boolean inWater;

    public LeapingGrubShrimpJumpGoal(LeapingGrubShrimpEntity entity, int chance) {
        this.entity = entity;
        this.chance = chance;
    }

    public boolean shouldExecute() {
        if (this.entity.getRNG().nextInt(this.chance) != 0) {
            return false;
        } else {
            Direction direction = this.entity.getAdjustedHorizontalFacing();
            int i = direction.getXOffset();
            int j = direction.getZOffset();
            BlockPos blockpos = this.entity.getPosition();

            for(int k : JUMP_DISTANCES) {
                if (!this.canJumpTo(blockpos, i, j, k) || !this.isAirAbove(blockpos, i, j, k)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.add(dx * scale, 0, dz * scale);
        return this.entity.world.getFluidState(blockpos).isTagged(FluidTags.WATER) && !this.entity.world.getBlockState(blockpos).getMaterial().blocksMovement();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.entity.world.getBlockState(pos.add(dx * scale, 1, dz * scale)).isAir() && this.entity.world.getBlockState(pos.add(dx * scale, 2, dz * scale)).isAir();
    }

    public boolean shouldContinueExecuting() {
        double d0 = this.entity.getMotion().y;
        return (!(d0 * d0 < (double)0.03F) || this.entity.rotationPitch == 0.0F || !(Math.abs(this.entity.rotationPitch) < 10.0F) || !this.entity.isInWater()) && !this.entity.isOnGround();
    }

    public boolean isPreemptible() {
        return false;
    }

    public void startExecuting() {
        Direction direction = this.entity.getAdjustedHorizontalFacing();
        this.entity.setMotion(this.entity.getMotion().add((double)direction.getXOffset() * 0.6D, 0.7D, (double)direction.getZOffset() * 0.6D));
        this.entity.getNavigator().clearPath();
    }

    public void resetTask() {
        this.entity.rotationPitch = 0.0F;
    }

    public void tick() {
        boolean flag = this.inWater;
        if (!flag) {
            FluidState ifluidstate = this.entity.world.getFluidState(this.entity.getPosition());
            this.inWater = ifluidstate.isTagged(FluidTags.WATER);
        }

        if (this.inWater && !flag) {
            this.entity.playSound(SoundEvents.ENTITY_DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vector3d vec3d = this.entity.getMotion();
        if (vec3d.y * vec3d.y < (double)0.03F && this.entity.rotationPitch != 0.0F) {
            this.entity.rotationPitch = MathHelper.rotLerp(this.entity.rotationPitch, 0.0F, 0.2F);
        } else {
            double d0 = Math.sqrt(Entity.horizontalMag(vec3d));
            double d1 = Math.signum(-vec3d.y) * Math.acos(d0 / vec3d.length()) * (double)(180F / (float)Math.PI);
            this.entity.rotationPitch = (float)d1;
        }

    }
}
*/
