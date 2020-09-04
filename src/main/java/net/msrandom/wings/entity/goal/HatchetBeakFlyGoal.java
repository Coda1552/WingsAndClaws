package net.msrandom.wings.entity.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.util.math.Vec3d;
import net.msrandom.wings.entity.TameableDragonEntity;

import javax.annotation.Nullable;

public class HatchetBeakFlyGoal extends RandomWalkingGoal {
    public HatchetBeakFlyGoal(CreatureEntity creatureIn) {
        super(creatureIn, 1.0D, 20);
    }

    @Nullable
    @Override
    protected Vec3d getPosition() {
        if (this.creature.isInWaterOrBubbleColumn()) {
            Vec3d vec3d = RandomPositionGenerator.getLandPos(this.creature, 15, 7);
            return vec3d == null ? super.getPosition() : vec3d;
        }

        return this.creature.getRNG().nextFloat() >= 0.5 && !((TameableDragonEntity) creature).shouldSleep() ? RandomPositionGenerator.findAirTarget(creature, 32, 32, creature.getLookVec(), ((float) Math.PI / 2F), 15, 32) : RandomPositionGenerator.getLandPos(this.creature, 10, 7);
    }
}
