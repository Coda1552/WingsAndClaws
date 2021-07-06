package coda.wingsandclaws.entity.goal;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import coda.wingsandclaws.entity.util.TameableDragonEntity;

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
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.creature.isVehicle() || canFly != null && !canFly.test(creature)) {
            return false;
        } else {
            if (mustUpdate) {
                if (this.creature.isOrderedToSit()) {
                    goToGround();
                    return true;
                }

                Vector3d vec3d = RandomPositionGenerator.getAboveLandPos(this.creature, 48, 15, this.creature.getViewVector(0.0F), ((float) Math.PI / 2F), 12, 6);
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
        BlockPos height = this.creature.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, creature.blockPosition());
        this.x = height.getX();
        this.y = height.getY();
        this.z = height.getZ();
    }

    public boolean canContinueToUse() {
        return this.creature.distanceToSqr(x, y, z) > 4 && !this.creature.getNavigation().isDone() && !this.creature.isVehicle();
    }

    public void start() {
        this.creature.getNavigation().moveTo(this.x, this.y, this.z, 0.8);
    }

    public void stop() {
        super.stop();
        this.creature.getNavigation().stop();
        this.mustUpdate = true;
    }
}
