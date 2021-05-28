package net.coda.wings.entity;

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
import net.coda.wings.item.WingsItems;

import javax.annotation.Nullable;

public abstract class TameableDragonEntity extends TameableEntity implements IDragonEntity {
    public static final byte HEAL_PARTICLES_ID = 9;
    private static final DataParameter<Boolean> GENDER = EntityDataManager.createKey(TameableDragonEntity.class, DataSerializers.BOOLEAN);

    protected TameableDragonEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(GENDER, false);
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (isSleeping() && getHealth() < getMaxHealth() && getRNG().nextDouble() < 0.008) {
            world.setEntityState(this, HEAL_PARTICLES_ID);
            heal(1);
        }
    }

    @Override
    protected boolean isMovementBlocked() {
        return isSleeping() || super.isMovementBlocked();
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        float maxHealth = this.getMaxHealth();
        float health = this.getHealth();
        if (isTamed()) {
            if (stack.getItem() == WingsItems.SUGARSCALE.get()) {
                if (health < maxHealth) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    heal(4);
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                    return ActionResultType.SUCCESS;
                }
            } else if (player.isSneaking()) {
                this.func_233687_w_(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPath();
                this.setAttackTarget(null);
                return ActionResultType.SUCCESS;
            }
        }
        return super.func_230254_b_(player, hand);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == HEAL_PARTICLES_ID)
        {
            double d = getWidth() * getHeight();
            for (int i = 0; i < d * d; ++i)
            {
                double x = getPosX() + (2 * rand.nextDouble() - 1) * getWidth() + 0.4d;
                double y = getPosY() + getRNG().nextDouble() * getHeight();
                double z = getPosZ() + (2 * rand.nextDouble() - 1) * getWidth() + 0.4d;
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
            }
        }
        super.handleStatusUpdate(id);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        setRandomGender(spawnDataIn);
        return spawnDataIn;
    }

    protected void setRandomGender(ILivingEntityData spawnDataIn) {
        this.setGender(Gender.fromBool(rand.nextBoolean()));
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
        compound.putBoolean("Gender", this.getGender().toBool());
        super.writeAdditional(compound);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        this.setGender(Gender.fromBool(compound.getBoolean("Gender")));
        super.readAdditional(compound);
    }

    public Gender getGender() {
        return Gender.fromBool(this.dataManager.get(GENDER));
    }

    public void setGender(Gender gender) {
        this.dataManager.set(GENDER, gender.toBool());
    }

    public boolean shouldSleep() {
        return world.getDayTime() > 13000 && world.getDayTime() < 23000;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        if (ageable != this && getClass().isInstance(ageable)) {
            createEgg();
            resetInLove();
            setGrowingAge(600);
            ((AnimalEntity) ageable).resetInLove();
            ageable.setGrowingAge(600);
        }
        return null;
    }

    public boolean handleSpawnEgg(PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof SpawnEggItem && ((SpawnEggItem) stack.getItem()).hasType(stack.getTag(), this.getType())) {
            if (!this.world.isRemote) {
                TameableDragonEntity child = (TameableDragonEntity) getType().create(world);
                if (child != null) {
                    child.setGrowingAge(-24000);
                    child.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
                    child.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(child.getPosition()), SpawnReason.SPAWN_EGG, null, null);
                    this.world.addEntity(child);
                    if (stack.hasDisplayName()) {
                        child.setCustomName(stack.getDisplayName());
                    }

                    if (!player.abilities.isCreativeMode) {
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
