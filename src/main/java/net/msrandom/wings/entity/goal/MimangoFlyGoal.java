package net.msrandom.wings.entity.goal;

import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;

import java.util.EnumSet;

public class MimangoFlyGoal extends Goal {
    protected final MimangoEntity creature;
    protected double x;
    protected double y;
    protected double z;
    protected boolean mustUpdate = true;

    public MimangoFlyGoal(MimangoEntity creatureIn) {
        this.creature = creatureIn;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    public boolean canStart() {
        if (this.creature.hasPassengers() || this.creature.isHiding()) {
            return false;
        } else {
            if (mustUpdate) {
                if (this.creature.getState() == TameableDragonEntity.WanderState.STAY) {
                    goToGround();
                    return true;
                }

                Vec3d vec3d = TargetFinder.findAirTarget(this.creature, 48, 15, this.creature.getRotationVec(0.0F), ((float) Math.PI / 2F), 12, 6);
                if (vec3d == null) {
                    goToGround();
                } else {
                    this.x = vec3d.x;
                    this.y = vec3d.y;
                    this.z = vec3d.z;
                }

                this.mustUpdate = false;
                return true;
            }
            return true;
        }
    }

    private void goToGround() {
        BlockPos height = this.creature.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, creature.getBlockPos());
        this.x = height.getX() + 0.5;
        this.y = height.getY();
        this.z = height.getZ() + 0.5;
    }

    public boolean shouldContinue() {
        return this.creature.squaredDistanceTo(x, y, z) > 4 && !this.creature.getNavigation().isIdle() && !this.creature.hasPassengers();
    }

    public void start() {
        this.creature.getNavigation().startMovingTo(this.x, this.y, this.z, 0.8);
    }

    public void stop() {
        super.stop();
        this.creature.getNavigation().stop();
        this.mustUpdate = true;
    }
}