package coda.wingsandclaws.tileentity;

import coda.wingsandclaws.block.WingsBlocks;
import coda.wingsandclaws.entity.WingsEntities;
import coda.wingsandclaws.entity.monster.DumpyEggDrakeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DEDNestTileEntity extends NestTileEntity {
    private List<AtomicInteger> eggs = new ArrayList<>();
    private AtomicInteger current;

    public DEDNestTileEntity() {
        super(WingsTileEntities.DED_NEST.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putIntArray("Eggs", eggs.stream().map(AtomicInteger::get).collect(Collectors.toList()));
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        List<AtomicInteger> list = new ArrayList<>();
        for (int i : compound.getIntArray("Eggs")) list.add(new AtomicInteger(i));
        this.eggs = list;
    }

    public boolean addEgg() {
        if (getEggCount() < 3) {
            eggs.add(new AtomicInteger(6000));
            return true;
        }
        return false;
    }

    public boolean removeEgg() {
        if (getEggCount() > 0) {
            eggs.remove(0);
            current = null;
            return true;
        }
        return false;
    }

    public void setEggs(World world, int count) {
        for (int i = 0; i < Math.min(count, 3); i++) {
            eggs.add(new AtomicInteger(3600));
        }
        update(world);
    }

    private void update(World world) {
        IPacket<IClientPlayNetHandler> packet = Objects.requireNonNull(getUpdatePacket());
        Objects.requireNonNull(Objects.requireNonNull(world).getServer()).getPlayerList().getPlayers().forEach(player -> player.connection.sendPacket(packet));
    }

    public int getEggCount() {
        return eggs.size();
    }

    @Override
    public void tick() {
        if (current == null && getEggCount() > 0) current = eggs.get(0);
        World world = getWorld();
        if (world != null && !world.isRemote && current != null) {
            if (current.decrementAndGet() <= 0) {
                BlockPos pos = getPos();
                DumpyEggDrakeEntity drake = WingsEntities.DUMPY_EGG_DRAKE.get().create(world);
                if (drake != null) {
                    drake.setGrowingAge(-24000);
                    drake.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                    drake.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(pos), SpawnReason.NATURAL, null, null);
                    world.getEntitiesWithinAABB(PlayerEntity.class, drake.getBoundingBox().grow(15)).stream().reduce((p1, p2) -> drake.getDistanceSq(p1) < drake.getDistanceSq(p2) ? p1 : p2).ifPresent(drake::setTamedBy);
                    world.addEntity(drake);
                }
                eggs.remove(0);
                current = null;
                world.notifyBlockUpdate(pos, WingsBlocks.DED_NEST.get().getDefaultState(), WingsBlocks.DED_NEST.get().getDefaultState(), 3);
                markDirty();
            }
        }
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        read(WingsBlocks.DED_NEST.get().getDefaultState(), tag);
    }
}
