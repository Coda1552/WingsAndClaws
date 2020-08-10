package net.msrandom.wings.entity.goal;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.Heightmap;
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
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.creature.isBeingRidden() || this.creature.isHiding()) {
            return false;
        } else {
            if (mustUpdate) {
                if (this.creature.getState() == TameableDragonEntity.WanderState.STAY) {
                    goToGround();
                    return true;
                }

                Vec3d vec3d = RandomPositionGenerator.findAirTarget(this.creature, 48, 15, this.creature.getLook(0.0F), ((float) Math.PI / 2F), 12, 6);
                if (vec3d == null) {
                    return false;
                } else {
                    this.x = vec3d.x;
                    this.y = vec3d.y;
                    this.z = vec3d.z;
                    this.mustUpdate = false;
                    return true;
                }
            }
            return true;
        }
    }

    private void goToGround() {
        BlockPos height = this.creature.world.getHeight(Heightmap.Type.MOTION_BLOCKING, creature.getPosition());
        this.x = height.getX();
        this.y = height.getY();
        this.z = height.getZ();
        this.mustUpdate = false;
    }

    public boolean shouldContinueExecuting() {
        return this.creature.getDistanceSq(x, y, z) < 4 && !this.creature.getNavigator().noPath() && !this.creature.isBeingRidden();
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
