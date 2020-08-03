package net.msrandom.wings.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DEDNestBlockEntity extends NestBlockEntity {
    private List<AtomicInteger> eggs = new ArrayList<>();
    private AtomicInteger current;

    public DEDNestBlockEntity() {
        super(WingsBlockEntities.DED_NEST);
    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        compound.putIntArray("Eggs", eggs.stream().map(AtomicInteger::get).collect(Collectors.toList()));
        return super.toTag(compound);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag compound) {
        List<AtomicInteger> list = new ArrayList<>();
        for (int i : compound.getIntArray("Eggs")) list.add(new AtomicInteger(i));
        this.eggs = list;
        super.fromTag(state, compound);
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
                    drake.setBreedingAge(-24000);
                    drake.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                    drake.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);
                    world.getEntitiesWithinAABB(PlayerEntity.class, drake.getBoundingBox().grow(15)).stream().reduce((p1, p2) -> drake.squaredDistanceTo(p1) < drake.squaredDistanceTo(p2) ? p1 : p2).ifPresent(drake::setTamedBy);
                    world.spawnEntity(drake);
                }
                eggs.remove(0);
                current = null;
            }
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        read(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return write(new CompoundTag());
    }

    @Nullable
    @Override
    public SUpdateBlockEntityPacket getUpdatePacket() {
        return new SUpdateBlockEntityPacket(getPos(), 1, getUpdateTag());
    }
}
