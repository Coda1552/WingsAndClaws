package net.msrandom.wings.entity.goal;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.passive.MimangoEntity;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MinmangoFlyGoal extends Goal {
    protected final MimangoEntity creature;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected int executionChance;
    protected boolean mustUpdate;

    public MinmangoFlyGoal(MimangoEntity creatureIn, double speedIn) {
        this(creatureIn, speedIn, 120);
    }

    public MinmangoFlyGoal(MimangoEntity creatureIn, double speedIn, int chance) {
        this.creature = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    public boolean canStart() {
        if (this.creature.isBeingRidden() || this.creature.getState() != TameableDragonEntity.WanderState.WANDER) {
            return false;
        } else {
            Vec3d vec3d = this.getBlockPos();
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
    }

    @Nullable
    protected Vec3d getBlockPos() {
        return RandomPositionGenerator.findAirTarget(this.creature, 15, 15, this.creature.getLook(0.0F), ((float) Math.PI / 2F), 6, 3);
    }

    @Override
    public boolean shouldContinue() {
        return !this.creature.getNavigation().isIdle() && !this.creature.isBeingRidden();
    }

    public void start() {
        this.creature.getNavigation().startMovingTo(this.x, this.y, this.z, this.speed);
    }

    public void stop() {
        this.creature.getNavigation().stop();
        super.stop();
    }
}
