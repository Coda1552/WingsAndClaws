package net.msrandom.wings.entity.goal;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.tag.BlockTags;
import net.msrandom.wings.entity.passive.MimangoEntity;

public class MimangoHangGoal extends Goal {
    private final MimangoEntity entity;
    private final double playerDistance;
    private final TargetPredicate builtPredicate;

    private int ticksRunning;

    public MimangoHangGoal(MimangoEntity entity, double playerDistance) {
        this.entity = entity;
        this.playerDistance = playerDistance;
        this.builtPredicate = new TargetPredicate().setBaseMaxDistance(playerDistance).setPredicate(EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL::test);
    }

    @Override
    public boolean canStart() {
        return isPlayerNear() && this.entity.world.getBlockState(this.entity.getBlockPos().up()).isIn(BlockTags.LEAVES);
    }

    @Override
    public void start() {
        this.entity.setHanging(true);
    }

    @Override
    public void stop() {
        ticksRunning = 0;
        this.entity.setHanging(false);
    }

    @Override
    public void tick() {
        ticksRunning++;
        this.entity.getNavigation().stop();
    }

    private boolean isPlayerNear() {
        return this.entity.world.getClosestEntity(PlayerEntity.class, builtPredicate, this.entity, this.entity.getX(), this.entity.getY(), this.entity.getZ(), this.entity.getBoundingBox().expand(this.playerDistance, 3.0D, this.playerDistance)) == null;
    }
}
