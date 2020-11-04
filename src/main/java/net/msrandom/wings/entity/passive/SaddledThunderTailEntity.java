package net.msrandom.wings.entity.passive;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.item.WingsItems;

import javax.annotation.Nullable;

public class SaddledThunderTailEntity extends TameableDragonEntity {
    private static final EntitySize SLEEPING_SIZE = EntitySize.flexible(1.2f, 0.5f);
    private int alarmedTimer;
    private Vec3d oldPos;

    public SaddledThunderTailEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return isSleeping() ? SLEEPING_SIZE : super.getSize(poseIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.isTamed() ? 150 : 100);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
    }

    @Override
    protected void registerGoals() {
//        super.registerGoals();
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        boolean bullSpawn;
        if (spawnDataIn instanceof STTData) bullSpawn = ((STTData)spawnDataIn).canMaleSpawn();
        else {
            bullSpawn = rand.nextBoolean();
            spawnDataIn = new STTData();
        }

        AgeableData ageableData = (AgeableData)spawnDataIn;
        if (ageableData.getIndexInGroup() > 2) ageableData.setCanBabySpawn(true);
        if (bullSpawn) ((STTData) spawnDataIn).setCanMaleSpawn(false);

        this.setGender(bullSpawn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 10;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return isSleeping() ? null : WingsSounds.STT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.STT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.STT_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.20f;
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150);
            this.setHealth(150);
        } else {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof LivingEntity && (!(source.getTrueSource() instanceof PlayerEntity) || (!isOwner((LivingEntity) source.getTrueSource()) && !((PlayerEntity) source.getTrueSource()).abilities.isCreativeMode))) {
            if (!isChild() && getGender() && !isOwner((LivingEntity) source.getTrueSource()))
                setAttackTarget((LivingEntity) source.getTrueSource());
        }

        if (isSleeping()) {
            oldPos = getPositionVec();
            alarmedTimer = 200;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (handleSpawnEgg(player, stack)) return true;

        if (isChild() && !isTamed() && isBreedingItem(stack)) {
            this.consumeItemFromStack(player, stack);
            if (rand.nextInt(20) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                this.setTamedBy(player);
                this.navigator.clearPath();
                this.setAttackTarget(null);
                this.setHealth(150);
                this.world.setEntityState(this, (byte) 7);
            } else {
                this.world.setEntityState(this, (byte) 6);
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.PUMPKIN_PIE; //todo check if this is the right item
    }

    @Override
    public void livingTick() {
        if (oldPos != null) {
            setPosition(oldPos.x, oldPos.y, oldPos.z);
            oldPos = null;
        }
        if (!isSleeping()) {
            if (!world.isRemote) {
                if (isTamed()) {
                    if (getState() == WanderState.FOLLOW) {
                        LivingEntity owner = getOwner();
                        if (owner != null) {
                            getNavigator().tryMoveToEntityLiving(owner, 0.2);
                            if (onGround) {
                                double x = owner.getPosX() - getPosX();
                                double z = owner.getPosZ() - getPosZ();
                                setMotion(MathHelper.clamp(x, -0.2, 0.2), 0, MathHelper.clamp(z, -0.2, 0.2));

                                rotationYaw = (float) Math.toDegrees(Math.atan2(z, x) - Math.PI / 2);
                                renderYawOffset = rotationYaw;
                            }
                        }
                    }
                }
                if (alarmedTimer-- <= 0) alarmedTimer = 0;
            }
            super.livingTick();
        } else this.travel(new Vec3d(this.moveStrafing, this.moveVertical, this.moveForward));
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public boolean isSleeping() {
        return alarmedTimer == 0 && world.getDayTime() > 13000 && world.getDayTime() < 1000;
    }

    @Override
    public ItemStack getEgg() {
        return new ItemStack(WingsItems.SADDLED_THUNDER_TAIL_EGG);
    }

    public static class STTData extends AgeableData {
        public boolean canMaleSpawn = true;

        public STTData() {
            this.setCanBabySpawn(false);
            this.setBabySpawnProbability(0.5F);
        }

        public boolean canMaleSpawn() {
            return this.canMaleSpawn;
        }

        public void setCanMaleSpawn(boolean canMaleSpawn) {
            this.canMaleSpawn = canMaleSpawn;
        }
    }
}
