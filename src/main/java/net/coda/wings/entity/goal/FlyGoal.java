package net.coda.wings.entity.goal;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import net.coda.wings.entity.TameableDragonEntity;

import java.util.EnumSet;
import java.util.function.Predicate;

public class FlyGoal<T extends TameableDragonEntity> extends Goal {
    protected final T creature;
    private final Predicate<T> canFly;
    protected double x;
    protected double y;
    protected double z;
    protected boolean mustUpdate = true;

    public FlyGoal(T creatureIn, Predicate<T> canFly) {
        this.creature = creatureIn;
        this.canFly = canFly;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.creature.isBeingRidden() || canFly != null && !canFly.test(creature)) {
            return false;
        } else {
            if (mustUpdate) {
                if (this.creature.isSitting()) {
                    goToGround();
                    return true;
                }

                Vector3d vec3d = RandomPositionGenerator.findAirTarget(this.creature, 48, 15, this.creature.getLook(0.0F), ((float) Math.PI / 2F), 12, 6);
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
        BlockPos height = this.creature.world.getHeight(Heightmap.Type.MOTION_BLOCKING, creature.getPosition());
        this.x = height.getX();
        this.y = height.getY();
        this.z = height.getZ();
    }

    public boolean shouldContinueExecuting() {
        return this.creature.getDistanceSq(x, y, z) > 4 && !this.creature.getNavigator().noPath() && !this.creature.isBeingRidden();
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, 0.8);
    }

    public void resetTask() {
        super.resetTask();
        this.creature.getNavigator().clearPath();
        this.mustUpdate = true;
    }
}
