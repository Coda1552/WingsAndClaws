package random.wings.tileentity;

import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import random.wings.entity.DumpyEggDrakeEntity;
import random.wings.entity.WingsEntities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NestTileEntity extends TileEntity implements ITickableTileEntity {
    private int ticksExisted;
    private List<AtomicInteger> eggs = new ArrayList<>();
    private AtomicInteger current;

    public NestTileEntity() {
        super(WingsTileEntities.NEST);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Ticks", ticksExisted);
        compound.putIntArray("Eggs", eggs.stream().map(AtomicInteger::get).collect(Collectors.toList()));
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.ticksExisted = compound.getInt("Ticks");
        List<AtomicInteger> list = new ArrayList<>();
        for (int i : compound.getIntArray("Eggs")) list.add(new AtomicInteger(i));
        this.eggs = list;
        super.read(compound);
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
        if (hasWorld() && current != null) {
            if (current.decrementAndGet() <= 0) {
                World world = Objects.requireNonNull(getWorld());
                BlockPos pos = getPos();
                DumpyEggDrakeEntity drake = WingsEntities.DUMPY_EGG_DRAKE.create(world);
                if (drake != null) {
                    drake.setGrowingAge(-24000);
                    drake.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                    drake.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.NATURAL, null, null);
                    world.getEntitiesWithinAABB(PlayerEntity.class, drake.getBoundingBox().grow(15)).stream().reduce((p1, p2) -> drake.getDistanceSq(p1) < drake.getDistanceSq(p2) ? p1 : p2).ifPresent(drake::setTamedBy);
                    world.addEntity(drake);
                }
                eggs.remove(current);
                current = null;
            }
        }
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        read(tag);
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
}
