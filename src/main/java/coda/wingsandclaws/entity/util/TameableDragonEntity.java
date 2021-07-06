package coda.wingsandclaws.entity.util;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import coda.wingsandclaws.init.WingsItems;

import javax.annotation.Nullable;

public abstract class TameableDragonEntity extends TameableEntity implements IDragonEntity {
    public static final byte HEAL_PARTICLES_ID = 9;
    private static final DataParameter<Boolean> GENDER = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);

    protected TameableDragonEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GENDER, false);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (isSleeping() && getHealth() < getMaxHealth() && getRandom().nextDouble() < 0.008) {
            level.broadcastEntityEvent(this, HEAL_PARTICLES_ID);
            heal(1);
        }
    }

    @Override
    protected boolean isImmobile() {
        return isSleeping() || super.isImmobile();
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        float maxHealth = this.getMaxHealth();
        float health = this.getHealth();
        if (isTame()) {
            if (stack.getItem() == WingsItems.SUGARSCALE.get()) {
                if (health < maxHealth) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    heal(4);
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    return ActionResultType.SUCCESS;
                }
            } else if (player.isShiftKeyDown()) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return ActionResultType.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == HEAL_PARTICLES_ID)
        {
            double d = getBbWidth() * getBbHeight();
            for (int i = 0; i < d * d; ++i)
            {
                double x = getX() + (2 * random.nextDouble() - 1) * getBbWidth() + 0.4d;
                double y = getY() + getRandom().nextDouble() * getBbHeight();
                double z = getZ() + (2 * random.nextDouble() - 1) * getBbWidth() + 0.4d;
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            }
        }
        super.handleEntityEvent(id);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        setRandomGender(spawnDataIn);
        return spawnDataIn;
    }

    protected void setRandomGender(ILivingEntityData spawnDataIn) {
        this.setGender(Gender.fromBool(random.nextBoolean()));
    }

    @Override
    public boolean canMate(AnimalEntity otherAnimal) {
        if (otherAnimal.getClass() == this.getClass()) {
            TameableDragonEntity entity = (TameableDragonEntity) otherAnimal;
            return entity.isInLove() && this.isInLove() && this.getGender() != entity.getGender() && !this.isSleeping() && !entity.isSleeping();
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putBoolean("Gender", this.getGender().toBool());
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        this.setGender(Gender.fromBool(compound.getBoolean("Gender")));
        super.readAdditionalSaveData(compound);
    }

    public Gender getGender() {
        return Gender.fromBool(this.entityData.get(GENDER));
    }

    public void setGender(Gender gender) {
        this.entityData.set(GENDER, gender.toBool());
    }

    public boolean shouldSleep() {
        return level.getDayTime() > 13000 && level.getDayTime() < 23000;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageable) {
        if (ageable != this && getClass().isInstance(ageable)) {
            createEgg();
            resetLove();
            setAge(600);
            ((AnimalEntity) ageable).resetLove();
            ageable.setAge(600);
        }
        return null;
    }

    public boolean handleSpawnEgg(PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof SpawnEggItem && ((SpawnEggItem) stack.getItem()).spawnsEntity(stack.getTag(), this.getType())) {
            if (!this.level.isClientSide) {
                TameableDragonEntity child = (TameableDragonEntity) getType().create(level);
                if (child != null) {
                    child.setAge(-24000);
                    child.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                    child.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(child.blockPosition()), SpawnReason.SPAWN_EGG, null, null);
                    this.level.addFreshEntity(child);
                    if (stack.hasCustomHoverName()) {
                        child.setCustomName(stack.getHoverName());
                    }

                    if (!player.abilities.instabuild) {
                        stack.shrink(1);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public enum Gender {
        FEMALE,
        MALE;

        public static Gender fromBool(boolean bool) {
            return bool ? MALE : FEMALE;
        }

        public boolean toBool() {
            return this == MALE;
        }
    }
}
