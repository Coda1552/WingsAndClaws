package net.msrandom.wings.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.item.WingsItems;

import javax.annotation.Nullable;

public abstract class TameableDragonEntity extends TameableEntity implements IDragonEntity {
    private static final DataParameter<Byte> STATE = EntityDataManager.createKey(TameableDragonEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(TameableDragonEntity.class, DataSerializers.BOOLEAN);

    protected TameableDragonEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(STATE, (byte) 0);
        this.dataManager.register(GENDER, false);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setGender(rand.nextBoolean());
        return spawnDataIn;
    }

    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {
        if (otherAnimal.getClass() == this.getClass()) {
            TameableDragonEntity entity = (TameableDragonEntity) otherAnimal;
            return entity.isInLove() && this.isInLove() && this.getGender() != entity.getGender() && !this.isSleeping() && !entity.isSleeping();
        }
        return false;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putBoolean("Gender", this.getGender());
        super.writeAdditional(compound);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        this.setGender(compound.getBoolean("Gender"));
        super.readAdditional(compound);
    }

    public boolean getGender() {
        return this.dataManager.get(GENDER);
    }

    public void setGender(boolean gender) {
        this.dataManager.set(GENDER, gender);
    }

    public WonderState getState() {
        return WonderState.values()[dataManager.get(STATE)];
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == WingsItems.COMMAND_STAFF && isOwner(player)) {
            WonderState state = getState();
            WonderState newState = state == WonderState.FOLLOW ? WonderState.STAY : WonderState.values()[state.ordinal() + 1];
            dataManager.set(STATE, (byte) newState.ordinal());
            player.sendStatusMessage(new TranslationTextComponent("entity." + WingsAndClaws.MOD_ID + "state." + newState.name().toLowerCase()), true);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        createEgg();
        return null;
    }

    public enum WonderState {
        WONDER, STAY, FOLLOW
    }
}
