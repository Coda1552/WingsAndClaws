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
    private int ticksAfloat;

    public HatchetBeakWanderGoal(HatchetBeakEntity hatchetBeak) {
        this.hatchetBeak = hatchetBeak;
    }

    @Override
    public boolean canUse() {
        if (hatchetBeak.isVehicle()) {
            return false;
        }
        if (hatchetBeak.callerPosition != null) {
            return true;
        }
        if (hatchetBeak.level.random.nextInt(5) == 0) {
            boolean flying = hatchetBeak.isFlying() && !hatchetBeak.isShotDown();
            boolean grounded = !flying || hatchetBeak.tickCount <= 25;
            LivingEntity attackTarget = hatchetBeak.getTarget();
            if (attackTarget == null) {
                hatchetBeak.attackTimer = 0;
                if (!grounded && ticksAfloat >= flyTime) {
                    target = null;
                }
                if (target == null) {
                    if (!flying || hatchetBeak.level.random.nextInt(10) == 0) {
                        if (!grounded) ++ticksAfloat;
                        boolean land = (grounded && hatchetBeak.level.random.nextFloat() >= 0.05f) || ticksAfloat >= 300 && hatchetBeak.level.random.nextFloat() <= 0.7f;
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
    public void start() {
        move(target, 0.6);
    }

    @Override
    public void stop() {
        target = null;
        flyTime = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && hatchetBeak.distanceToSqr(target) > 8 && !hatchetBeak.isVehicle();
    }

    @Override
    public void tick() {
        if (hatchetBeak.callerPosition != null) {
            move(hatchetBeak.callerPosition, 1);
            if (hatchetBeak.distanceToSqr(hatchetBeak.callerPosition) <= 4) {
                hatchetBeak.callerPosition = null;
            }
        } else if (canContinueToUse()) {
            final BlockPos position = hatchetBeak.blockPosition();
            final double xDistance = hatchetBeak.getX() - target.x;
            final double zDistance = hatchetBeak.getZ() - target.z;
            if (hatchetBeak.isFlying() && xDistance * xDistance + zDistance * zDistance >= 64 && position.getY() - hatchetBeak.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, position).getY() < 4) {
                hatchetBeak.setDeltaMovement(hatchetBeak.getDeltaMovement().x(), 0.4, hatchetBeak.getDeltaMovement().z());
            }
            move(target, 0.6);
        }
        if (hatchetBeak.isFlying()) {
            hatchetBeak.setDeltaMovement(hatchetBeak.getDeltaMovement().multiply(1.05, 1.05, 1.05));
        }
        if (hatchetBeak.getFlyTimer() > 0) {
            hatchetBeak.setFlyTimer(hatchetBeak.getFlyTimer() - 1);
        }
    }

    private void move(Vector3d position, double speed) {
        hatchetBeak.getMoveControl().setWantedPosition(position.x(), position.y(), position.z(), speed);
        if (target.y() - hatchetBeak.getY() < 0) {
            hatchetBeak.setDeltaMovement(hatchetBeak.getDeltaMovement().add(0, Math.max(target.y() - hatchetBeak.getY(), -0.16), 0));
        }
    }

    @Nullable
    private Vector3d getTargetPosition(boolean land) {
        if (hatchetBeak.isInWaterOrBubble()) {
            Vector3d vec3d = RandomPositionGenerator.getLandPos(hatchetBeak, 15, 7);
            ticksAfloat = 0;
            return vec3d == null ? RandomPositionGenerator.getPos(hatchetBeak, 10, 7) : vec3d;
        }

        if (!hatchetBeak.shouldSleep() && !land) {
            flyTime = hatchetBeak.level.random.nextInt(1800) + 600;
            return getAirPosition();
        }

        ticksAfloat = 0;
        return RandomPositionGenerator.getLandPos(hatchetBeak, 10, 7);
    }

    private Vector3d getAirPosition() {
        BlockPos pos = hatchetBeak.blockPosition().above(hatchetBeak.getY() > hatchetBeak.level.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) hatchetBeak.getX(), (int) hatchetBeak.getZ()) + 20 ? 0 : hatchetBeak.level.random.nextInt(12) + 12);
        int original = pos.getY();
        while (pos.getY() <= original - 8 && !hatchetBeak.level.isEmptyBlock(pos)) {
            pos = pos.below();
        }

        if (pos.getY() == original - 8) {
            pos = pos.above(8);

            while (pos.getY() <= original + 8 && !hatchetBeak.level.isEmptyBlock(pos)) {
                pos = pos.above();
            }

            if (pos.getY() == original + 8) return null;
        }

        LivingEntity owner = hatchetBeak.getOwner();
        double ownerDistanceX = 0;
        double ownerDistanceZ = 0;
        if (owner != null) {
            ownerDistanceX = owner.getX() - hatchetBeak.getX();
            ownerDistanceZ = owner.getZ() - hatchetBeak.getZ();
        }
        int xDistance = Math.abs(ownerDistanceX) > 32 ? (int) ownerDistanceX : hatchetBeak.level.random.nextInt(32) + 64;
        int zDistance = Math.abs(ownerDistanceZ) > 32 ? (int) ownerDistanceZ : hatchetBeak.level.random.nextInt(32) + 64;
        double rotation = Math.toRadians(ownerDistanceX * ownerDistanceX + ownerDistanceZ * ownerDistanceZ > 1024 ? MathHelper.atan2(ownerDistanceZ, ownerDistanceX) + 90 : hatchetBeak.level.random.nextInt(361));
        return new Vector3d(pos.getX() + Math.sin(rotation) * xDistance, pos.getY(), pos.getZ() + Math.cos(-rotation) * zDistance);
    }
}
