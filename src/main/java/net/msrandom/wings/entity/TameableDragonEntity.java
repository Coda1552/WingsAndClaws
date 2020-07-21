package net.msrandom.wings.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class TameableDragonEntity extends TameableEntity implements IDragonEntity {
    private static final TrackedData<Byte> STATE = DataTracker.registerData(TameableDragonEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> GENDER = DataTracker.registerData(TameableDragonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected TameableDragonEntity(EntityType<? extends TameableDragonEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STATE, (byte) 0);
        this.dataTracker.startTracking(GENDER, false);
    }

    @Override
    public EntityData initialize(WorldAccess world, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, CompoundTag dataTag) {
        spawnDataIn = super.initialize(world, difficultyIn, reason, spawnDataIn, dataTag);
        this.setGender(random.nextBoolean());
        return spawnDataIn;
    }

    @Override
    public boolean canBreedWith(AnimalEntity otherAnimal) {
        if (otherAnimal.getClass() == this.getClass()) {
            TameableDragonEntity entity = (TameableDragonEntity) otherAnimal;
            return entity.isInLove() && this.isInLove() && this.getGender() != entity.getGender() && !this.isSleeping() && !entity.isSleeping();
        }
        return false;
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compound) {
        compound.putBoolean("Gender", this.getGender());
        super.writeCustomDataToTag(compound);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound) {
        this.setGender(compound.getBoolean("Gender"));
        super.readCustomDataFromTag(compound);
    }

    public boolean getGender() {
        return this.dataTracker.get(GENDER);
    }

    public void setGender(boolean gender) {
        this.dataTracker.set(GENDER, gender);
    }

    public WanderState getState() {
        return WanderState.values()[dataTracker.get(STATE)];
    }

    public void setState(WanderState state) {
        dataTracker.set(STATE, (byte) state.ordinal());
    }

    @Override
    public PassiveEntity createChild(PassiveEntity ageable) {
        createEgg();
        return null;
    }

    public enum WanderState {
        WANDER, STAY, FOLLOW
    }
}
