package coda.wingsandclaws.tileentity;

import coda.wingsandclaws.init.WingsEntities;
import coda.wingsandclaws.entity.HatchetBeakEntity;
import coda.wingsandclaws.init.WingsTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class HBNestTileEntity extends NestTileEntity {
    private int eggTimer = -1;

    public HBNestTileEntity() {
        super(WingsTileEntities.HB_NEST.get());
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("EggTime", eggTimer);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        this.eggTimer = compound.getInt("EggTime");
        super.load(state, compound);
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
        World world = getLevel();
        if (world != null && !world.isClientSide && eggTimer != -1 && --eggTimer <= 0) {
            BlockPos pos = getBlockPos();
            HatchetBeakEntity hatchetBeak = WingsEntities.HATCHET_BEAK.get().create(world);
            if (hatchetBeak != null) {
                hatchetBeak.setAge(-24000);
                hatchetBeak.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                hatchetBeak.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(pos), SpawnReason.NATURAL, null, null);
                world.getEntitiesOfClass(PlayerEntity.class, hatchetBeak.getBoundingBox().inflate(15)).stream().reduce((p1, p2) -> hatchetBeak.distanceToSqr(p1) < hatchetBeak.distanceToSqr(p2) ? p1 : p2).ifPresent(hatchetBeak::tame);
                world.addFreshEntity(hatchetBeak);
            }
            eggTimer = -1;
        }
    }
}
