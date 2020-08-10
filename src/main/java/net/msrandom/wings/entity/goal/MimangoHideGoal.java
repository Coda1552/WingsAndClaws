package net.msrandom.wings.entity.goal;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.entity.passive.MimangoEntity;

public class MimangoHideGoal extends Goal {
    private final MimangoEntity entity;
    private final double playerDistance;
    private final EntityPredicate builtPredicate;
    private BlockPos lastPos;

    public MimangoHideGoal(MimangoEntity entity, double playerDistance) {
        this.entity = entity;
        this.playerDistance = playerDistance;
        this.builtPredicate = new EntityPredicate().setDistance(playerDistance).setCustomPredicate(EntityPredicates.CAN_AI_TARGET::test);
    }

    @Override
    public boolean shouldExecute() {
        boolean scared = isPlayerNear();
        if (!scared && !entity.isTamed()) return false;

        BlockPos pos = entity.getPosition();
        if (lastPos == null) {
            if (validBlock(pos.up())) {
                lastPos = pos;
            }
        } else {
            if (!lastPos.equals(pos) && entity.world.getBlockState(lastPos).isAir(entity.world, lastPos)) {
                entity.getNavigator().tryMoveToXYZ(lastPos.getX(), lastPos.getY(), lastPos.getZ(), 0.5);
            }
        }

        if (validBlock(pos.up()) && (scared || entity.getRNG().nextInt(10) == 0)) {
            return true;
        } else if (lastPos == null) {
            for (BlockPos blockPos : BlockPos.getAllInBoxMutable(pos.add(-16, -16, -16), pos.add(16, 16, 16))) {
                if (validBlock(blockPos)) {
                    lastPos = blockPos.down();
                    break;
                }
            }
        }
        return false;
    }

    private boolean validBlock(BlockPos pos) {
        return this.entity.world.getBlockState(pos).isIn(BlockTags.LEAVES);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (isPlayerNear() || entity.isTamed()) && validBlock(entity.getPosition().up()) && entity.getRNG().nextInt(100) != 0;
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
        lastPos = null;
    }

    private boolean isPlayerNear() {
        return this.entity.world.func_225318_b(PlayerEntity.class, builtPredicate, this.entity, this.entity.getPosX(), this.entity.getPosY(), this.entity.getPosZ(), this.entity.getBoundingBox().grow(this.playerDistance, 3.0D, this.playerDistance)) == null;
    }
}
