package net.msrandom.wings.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

import java.util.Objects;

public class HBNestBlockEntity extends NestBlockEntity {
    private int eggTimer = -1;

    public HBNestBlockEntity() {
        super(WingsBlockEntities.HB_NEST);
    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        compound.putInt("EggTime", eggTimer);
        return super.toTag(compound);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag compound) {
        this.eggTimer = compound.getInt("EggTime");
        super.fromTag(state, compound);
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
                    hatchetBeak.setBreedingAge(-24000);
                    hatchetBeak.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                    hatchetBeak.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);
                    world.getNonSpectatingEntities(PlayerEntity.class, hatchetBeak.getBoundingBox().expand(15)).stream().reduce((p1, p2) -> hatchetBeak.squaredDistanceTo(p1) < hatchetBeak.squaredDistanceTo(p2) ? p1 : p2).ifPresent(hatchetBeak::setOwner);
                    world.spawnEntity(hatchetBeak);
                }
                eggTimer = -1;
            }
        }
    }
}
