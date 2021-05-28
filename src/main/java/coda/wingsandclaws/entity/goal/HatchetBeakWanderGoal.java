package coda.wingsandclaws.entity.goal;

import coda.wingsandclaws.entity.HatchetBeakEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;

public class HatchetBeakWanderGoal extends Goal {
    private final HatchetBeakEntity hatchetBeak;
    private Vector3d target;
    private int flyTime;

    public HatchetBeakWanderGoal(HatchetBeakEntity hatchetBeak) {
        this.hatchetBeak = hatchetBeak;
    }

    @Override
    public boolean shouldExecute() {
        if (hatchetBeak.isBeingRidden()) {
            return false;
        }
        if (hatchetBeak.callerPosition != null) {
            return true;
        }
        if (hatchetBeak.world.rand.nextInt(5) == 0) {
            boolean flying = hatchetBeak.isFlying() && !hatchetBeak.isShotDown();
            boolean grounded = !flying || hatchetBeak.ticksExisted <= 25;
            LivingEntity attackTarget = hatchetBeak.getAttackTarget();
            if (attackTarget == null) {
                hatchetBeak.attackTimer = 0;
                if (!grounded && hatchetBeak.ticksAfloat >= flyTime) {
                    target = null;
                }
                if (target == null) {
                    if (!flying || hatchetBeak.world.rand.nextInt(10) == 0) {
                        if (!grounded) ++hatchetBeak.ticksAfloat;
                        boolean land = (grounded && hatchetBeak.world.rand.nextFloat() >= 0.05f) || hatchetBeak.ticksAfloat >= 300 && hatchetBeak.world.rand.nextFloat() <= 0.7f;
                        Vector3d target = getTargetPosition(land);
                        if (target != null) {
                            if (!flying && !hatchetBeak.shouldSleep() && !land) {
                                hatchetBeak.setFlyTimer(10);
                            }
                            this.target = target;
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void startExecuting() {
        move(target, 0.6);
    }

    @Override
    public void resetTask() {
        target = null;
        flyTime = 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return target != null && hatchetBeak.getDistanceSq(target) > 8 && !hatchetBeak.isBeingRidden();
    }

    @Override
    public void tick() {
        if (hatchetBeak.callerPosition != null) {
            move(hatchetBeak.callerPosition, 1);
            if (hatchetBeak.getDistanceSq(hatchetBeak.callerPosition) <= 4) {
                hatchetBeak.callerPosition = null;
            }
        } else if (shouldContinueExecuting()) {
            final BlockPos position = hatchetBeak.getPosition();
            final double xDistance = hatchetBeak.getPosX() - target.x;
            final double zDistance = hatchetBeak.getPosZ() - target.z;
            if (hatchetBeak.isFlying() && xDistance * xDistance + zDistance * zDistance >= 64 && position.getY() - hatchetBeak.world.getHeight(Heightmap.Type.MOTION_BLOCKING, position).getY() < 4) {
                hatchetBeak.setMotion(hatchetBeak.getMotion().getX(), 0.4, hatchetBeak.getMotion().getZ());
            }
            move(target, 0.6);
        }
        if (hatchetBeak.isFlying()) {
            hatchetBeak.setMotion(hatchetBeak.getMotion().mul(1.05, 1.05, 1.05));
        }
        if (hatchetBeak.getFlyTimer() > 0) {
            hatchetBeak.setFlyTimer(hatchetBeak.getFlyTimer() - 1);
        }
    }

    private void move(Vector3d position, double speed) {
        hatchetBeak.getMoveHelper().setMoveTo(position.getX(), position.getY(), position.getZ(), speed);
        if (target.getY() - hatchetBeak.getPosY() < 0) {
            hatchetBeak.setMotion(hatchetBeak.getMotion().add(0, Math.max(target.getY() - hatchetBeak.getPosY(), -0.16), 0));
        }
    }

    @Nullable
    private Vector3d getTargetPosition(boolean land) {
        if (hatchetBeak.isInWaterOrBubbleColumn()) {
            Vector3d vec3d = RandomPositionGenerator.getLandPos(hatchetBeak, 15, 7);
            hatchetBeak.ticksAfloat = 0;
            return vec3d == null ? RandomPositionGenerator.findRandomTarget(hatchetBeak, 10, 7) : vec3d;
        }

        if (!hatchetBeak.shouldSleep() && !land) {
            flyTime = hatchetBeak.world.rand.nextInt(1800) + 600;
            return getAirPosition();
        }

        hatchetBeak.ticksAfloat = 0;
        return RandomPositionGenerator.getLandPos(hatchetBeak, 10, 7);
    }

    private Vector3d getAirPosition() {
        BlockPos pos = hatchetBeak.getPosition().up(hatchetBeak.getPosY() > hatchetBeak.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) hatchetBeak.getPosX(), (int) hatchetBeak.getPosZ()) + 20 ? 0 : hatchetBeak.world.rand.nextInt(12) + 12);
        int original = pos.getY();
        while (pos.getY() <= original - 8 && !hatchetBeak.world.isAirBlock(pos)) {
            pos = pos.down();
        }

        if (pos.getY() == original - 8) {
            pos = pos.up(8);

            while (pos.getY() <= original + 8 && !hatchetBeak.world.isAirBlock(pos)) {
                pos = pos.up();
            }

            if (pos.getY() == original + 8) return null;
        }

        LivingEntity owner = hatchetBeak.getOwner();
        double ownerDistanceX = 0;
        double ownerDistanceZ = 0;
        if (owner != null) {
            ownerDistanceX = owner.getPosX() - hatchetBeak.getPosX();
            ownerDistanceZ = owner.getPosZ() - hatchetBeak.getPosZ();
        }
        int xDistance = Math.abs(ownerDistanceX) > 32 ? (int) ownerDistanceX : hatchetBeak.world.rand.nextInt(32) + 64;
        int zDistance = Math.abs(ownerDistanceZ) > 32 ? (int) ownerDistanceZ : hatchetBeak.world.rand.nextInt(32) + 64;
        double rotation = Math.toRadians(ownerDistanceX * ownerDistanceX + ownerDistanceZ * ownerDistanceZ > 1024 ? MathHelper.atan2(ownerDistanceZ, ownerDistanceX) + 90 : hatchetBeak.world.rand.nextInt(361));
        return new Vector3d(pos.getX() + Math.sin(rotation) * xDistance, pos.getY(), pos.getZ() + Math.cos(-rotation) * zDistance);
    }
}
