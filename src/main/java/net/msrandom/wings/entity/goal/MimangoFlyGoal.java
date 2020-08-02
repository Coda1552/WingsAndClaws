package net.msrandom.wings.entity.goal;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.Heightmap;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MimangoFlyGoal extends Goal {
    protected final MimangoEntity creature;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected int executionChance;
    protected boolean mustUpdate;

    public MimangoFlyGoal(MimangoEntity creatureIn, double speedIn) {
        this(creatureIn, speedIn, 120);
    }

    public MimangoFlyGoal(MimangoEntity creatureIn, double speedIn, int chance) {
        this.creature = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.creature.isBeingRidden() || this.creature.isHiding()) {
            return false;
        } else {
            if (this.creature.getState() == TameableDragonEntity.WanderState.STAY) {
                BlockPos height = this.creature.world.getHeight(Heightmap.Type.MOTION_BLOCKING, creature.getPosition());
                this.x = height.getX();
                this.y = height.getY();
                this.z = height.getZ();
                this.mustUpdate = false;
                return true;
            }

            Vec3d vec3d = this.getPosition();
            if (vec3d == null || creature.world.getBlockState(new BlockPos(vec3d.getX(), vec3d.getY() + 1, vec3d.getZ())).isSolid()) {
                return false;
            } else {
                this.x = vec3d.x;
                this.y = vec3d.y;
                this.z = vec3d.z;
                this.mustUpdate = false;
                return true;
            }
        }
    }

    @Nullable
    protected Vec3d getPosition() {
        return RandomPositionGenerator.findAirTarget(this.creature, 15, 15, this.creature.getLook(0.0F), ((float)Math.PI / 2F), 6, 3);
    }

    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath() && !this.creature.isBeingRidden();
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
    }

    public void resetTask() {
        this.creature.getNavigator().clearPath();
        super.resetTask();
    }
}
