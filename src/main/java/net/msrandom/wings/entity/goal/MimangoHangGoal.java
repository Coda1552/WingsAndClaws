package net.msrandom.wings.entity.goal;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EntityPredicates;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.entity.passive.MimangoEntity;

public class MimangoHangGoal extends Goal {
    private final MimangoEntity entity;
    private final double playerDistance;
    private final EntityPredicate builtPredicate;

    public MimangoHangGoal(MimangoEntity entity, double playerDistance) {
        this.entity = entity;
        this.playerDistance = playerDistance;
        this.builtPredicate = new EntityPredicate().setDistance(playerDistance).setCustomPredicate(EntityPredicates.CAN_AI_TARGET::test);
    }

    @Override
    public boolean shouldExecute() {
        return isPlayerNear() && this.entity.world.getBlockState(this.entity.getPosition().up()).isIn(BlockTags.LEAVES);
    }

    @Override
    public void startExecuting() {
        this.entity.getNavigator().clearPath();
        this.entity.setHiding(true);
        this.entity.playSound(WingsSounds.MIMANGO_HIDE, 1, 1);
    }

    @Override
    public void resetTask() {
        this.entity.setHiding(false);
        this.entity.playSound(WingsSounds.MIMANGO_UNHIDE, 1, 1);
    }

    private boolean isPlayerNear() {
        return this.entity.world.func_225318_b(PlayerEntity.class, builtPredicate, this.entity, this.entity.getPosX(), this.entity.getPosY(), this.entity.getPosZ(), this.entity.getBoundingBox().grow(this.playerDistance, 3.0D, this.playerDistance)) == null;
    }
}
