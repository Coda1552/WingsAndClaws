package net.msrandom.wings.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DEDNestBlockEntity extends NestBlockEntity implements BlockEntityClientSerializable {
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
        fromTag(compound);
        super.fromTag(state, compound);
    }

    public void fromTag(CompoundTag compound) {
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
        BlockEntityUpdateS2CPacket packet = new BlockEntityUpdateS2CPacket();
        Objects.requireNonNull(Objects.requireNonNull(world).getServer()).getPlayerManager().getPlayerList().forEach(player -> player.networkHandler.sendPacket(packet));
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
                    drake.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                    drake.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);
                    world.getNonSpectatingEntities(PlayerEntity.class, drake.getBoundingBox().expand(15)).stream().reduce((p1, p2) -> drake.squaredDistanceTo(p1) < drake.squaredDistanceTo(p2) ? p1 : p2).ifPresent(drake::setOwner);
                    world.spawnEntity(drake);
                }
                eggs.remove(0);
                current = null;
            }
        }
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putIntArray("Eggs", eggs.stream().map(AtomicInteger::get).collect(Collectors.toList()));
        return compoundTag;
    }
}
