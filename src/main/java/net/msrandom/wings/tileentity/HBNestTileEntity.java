package net.msrandom.wings.tileentity;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

import java.util.Objects;

public class HBNestTileEntity extends NestTileEntity {
    private int eggTimer = -1;

    public HBNestTileEntity() {
        super(WingsTileEntities.HB_NEST);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("EggTime", eggTimer);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.eggTimer = compound.getInt("EggTime");
        super.read(compound);
    }

    @Override
    public boolean addEgg() {
        if (eggTimer == -1) {
            eggTimer = 18000;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEgg() {
        if (eggTimer != -1) {
            eggTimer = -1;
            return true;
        }
        return false;
    }

    @Override
    public int getEggCount() {
        return eggTimer == -1 ? 0 : 1;
    }

    @Override
    public void tick() {
        if (hasWorld() && eggTimer != -1) {
            if (--eggTimer <= 0) {
                World world = Objects.requireNonNull(getWorld());
                BlockPos pos = getPos();
                HatchetBeakEntity hatchetBeak = WingsEntities.HATCHET_BEAK.create(world);
                if (hatchetBeak != null) {
                    hatchetBeak.setGrowingAge(-24000);
                    hatchetBeak.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                    hatchetBeak.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.NATURAL, null, null);
                    world.getEntitiesWithinAABB(PlayerEntity.class, hatchetBeak.getBoundingBox().grow(15)).stream().reduce((p1, p2) -> hatchetBeak.getDistanceSq(p1) < hatchetBeak.getDistanceSq(p2) ? p1 : p2).ifPresent(hatchetBeak::setTamedBy);
                    world.addEntity(hatchetBeak);
                }
                eggTimer = -1;
            }
        }
    }
}
