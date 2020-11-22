package net.msrandom.wings.entity.goal;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.msrandom.wings.client.WingsSounds;
import net.msrandom.wings.entity.passive.MimangoEntity;

public class MimangoHideGoal extends Goal {
    private final MimangoEntity entity;
    private final double playerDistance;
    private final EntityPredicate builtPredicate;
    private BlockPos target;
    private boolean searched;

    public MimangoHideGoal(MimangoEntity entity, double playerDistance) {
        this.entity = entity;
        this.playerDistance = playerDistance;
        this.builtPredicate = new EntityPredicate().setDistance(playerDistance).setCustomPredicate(EntityPredicates.CAN_AI_TARGET::test);
    }

    @Override
    public boolean shouldExecute() {
        boolean scared = isPlayerNear();
        if (!scared && !entity.isTamed()) return false;

        if (searched) {
            if (entity.ticksExisted % 20 != 0) {
                return false;
            }

            searched = false;
        }

        BlockPos pos = entity.getPosition();
        if (target == null) {
            if (validBlock(pos.up())) {
                target = pos;
            }

            searched = true;
        }

        if (target != null && (scared || entity.getRNG().nextInt(10) == 0)) {
            return true;
        } else if (target == null) {
            for (BlockPos blockPos : BlockPos.getAllInBoxMutable(pos.add(-16, -16, -16), pos.add(16, 16, 16))) {
                if (validBlock(blockPos)) {
                    target = blockPos.down();
                    break;
                }
            }
            searched = true;
        }
        return false;
    }

    private boolean validBlock(BlockPos pos) {
        BlockPos down = pos.down();
        return this.entity.world.getBlockState(pos).isIn(BlockTags.LEAVES) && entity.world.getBlockState(down).isAir(entity.world, down);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return entity.getRNG().nextInt(100) != 0 && validBlock(entity.getPosition().up()) && (entity.isTamed() || entity.ticksExisted % 20 != 0 || isPlayerNear());
    }

    @Override
    public void startExecuting() {
        if (target.equals(entity.getPosition())) {
            this.entity.getNavigator().clearPath();
            this.entity.setHiding(true);
            this.entity.playSound(WingsSounds.MIMANGO_HIDE, 1, 1);
        } else {
            entity.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), 0.5);
        }
    }

    @Override
    public void resetTask() {
        this.entity.setHiding(false);
        this.entity.playSound(WingsSounds.MIMANGO_UNHIDE, 1, 1);
        target = null;
    }

    private boolean isPlayerNear() {
        return this.entity.world.func_225318_b(PlayerEntity.class, builtPredicate, this.entity, this.entity.getPosX(), this.entity.getPosY(), this.entity.getPosZ(), this.entity.getBoundingBox().grow(this.playerDistance, 3.0D, this.playerDistance)) == null;
    }
}
