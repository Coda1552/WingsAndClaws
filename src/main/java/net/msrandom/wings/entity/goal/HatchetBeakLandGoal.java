package net.msrandom.wings.entity.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

public class HatchetBeakLandGoal extends Goal {
    private final TameableDragonEntity entity;

    public HatchetBeakLandGoal(TameableDragonEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() {
        return entity.shouldSleep() || entity.getRNG().nextInt(120) == 0;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        BlockPos height = this.entity.world.getHeight(Heightmap.Type.MOTION_BLOCKING, entity.getPosition());
        entity.getNavigator().tryMoveToXYZ(height.getX() + 0.5, height.getY(), height.getZ() + 0.5, 0.3);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        entity.getNavigator().clearPath();
    }
}
