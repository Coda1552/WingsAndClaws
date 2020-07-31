package net.msrandom.wings.entity.goal;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EntityPredicates;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.entity.passive.MimangoEntity;

import java.util.function.Predicate;

public class MimangoHangGoal extends Goal {
    private final MimangoEntity entity;
    private final double playerDistance;
    private final EntityPredicate builtPredicate;

    private int ticksRunning;

    public MimangoHangGoal(MimangoEntity entity, double playerDistance) {
        this.entity = entity;

        this.playerDistance = playerDistance;
        Predicate<LivingEntity> entityPredicate = EntityPredicates.CAN_AI_TARGET::test;
        this.builtPredicate = (new EntityPredicate()).setDistance(playerDistance).setCustomPredicate(entityPredicate.and((entity1) -> {
            return true;
        }));
    }

    @Override
    public boolean shouldExecute() {
        return isPlayerNear() && this.entity.world.getBlockState(this.entity.getPosition().up()).isIn(BlockTags.LEAVES);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return ticksRunning % 10 == 0 && isPlayerNear() && this.entity.world.getBlockState(this.entity.getPosition().up()).isIn(BlockTags.LEAVES);
    }

    @Override
    public void startExecuting() {
        this.entity.setHiding(true);
        this.entity.playSound(WingsSounds.MIMANGO_HIDE, 1, 1);
    }

    @Override
    public void resetTask() {
        ticksRunning = 0;
        this.entity.setHiding(false);
        this.entity.playSound(WingsSounds.MIMANGO_UNHIDE, 1, 1);
    }

    @Override
    public void tick() {
        ticksRunning++;
        this.entity.getNavigator().clearPath();
    }

    private boolean isPlayerNear() {
        return this.entity.world.func_225318_b(PlayerEntity.class, builtPredicate, this.entity, this.entity.getPosX(), this.entity.getPosY(), this.entity.getPosZ(), this.entity.getBoundingBox().grow(this.playerDistance, 3.0D, this.playerDistance)) == null;
    }
}
