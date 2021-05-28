package coda.wingsandclaws.entity.passive;

import coda.wingsandclaws.init.WingsSounds;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

//TODO use the right navigator and rely on goals for following the player
public class SaddledThunderTailEntity extends TameableDragonEntity {
    private static final EntitySize SLEEPING_SIZE = EntitySize.flexible(1.2f, 0.5f);
    private int alarmedTimer;
    private Vector3d oldPos;

    public SaddledThunderTailEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return isSleeping() ? SLEEPING_SIZE : super.getSize(poseIn);
    }

    public static AttributeModifierMap.MutableAttribute registerSTTAttributes() {
        return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23).createMutableAttribute(Attributes.MAX_HEALTH, 150).createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        boolean bullSpawn;
        if (spawnDataIn instanceof STTData) {
            bullSpawn = ((STTData) spawnDataIn).canMaleSpawn();
        } else {
            bullSpawn = rand.nextBoolean();
            spawnDataIn = new STTData();
        }

        AgeableData ageableData = (AgeableData) spawnDataIn;

        if (ageableData.getIndexInGroup() > 2) {
            this.setGrowingAge(-24000);
        }

        if (bullSpawn) {
            ((STTData) spawnDataIn).setCanMaleSpawn(false);
        }

        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void setRandomGender(ILivingEntityData spawnDataIn) {
        if (spawnDataIn instanceof STTData) {
            this.setGender(((STTData) spawnDataIn).canMaleSpawn() ? Gender.FEMALE : Gender.MALE);
        }
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 10;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return isSleeping() ? null : WingsSounds.STT_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.STT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.STT_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.20f;
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200);
            this.setHealth(150);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(150);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof LivingEntity && (!(source.getTrueSource() instanceof PlayerEntity) || (!isOwner((LivingEntity) source.getTrueSource()) && !((PlayerEntity) source.getTrueSource()).abilities.isCreativeMode))) {
            if (!isChild() && getGender() == Gender.MALE && !isOwner((LivingEntity) source.getTrueSource()))
                setAttackTarget((LivingEntity) source.getTrueSource());
        }

        if (isSleeping()) {
            oldPos = getPositionVec();
            alarmedTimer = 200;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (handleSpawnEgg(player, stack)) return ActionResultType.SUCCESS;

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
            return ActionResultType.SUCCESS;
        }
        return super.func_230254_b_(player, hand);
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
                    if (!isSitting()) {
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
        } else this.travel(new Vector3d(this.moveStrafing, this.moveVertical, this.moveForward));
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public boolean isSleeping() {
        return alarmedTimer == 0 && world.getDayTime() > 13000 && world.getDayTime() < 1000;
    }

/*    @Override
    public ItemStack getEgg() {
        return new ItemStack(WingsItems.SADDLED_THUNDER_TAIL_EGG);
    }*/

    public static class STTData extends AgeableData {
        public boolean canMaleSpawn = true;

        public STTData() {
            super(false);
        }

        public boolean canMaleSpawn() {
            return this.canMaleSpawn;
        }

        public void setCanMaleSpawn(boolean canMaleSpawn) {
            this.canMaleSpawn = canMaleSpawn;
        }

        @Override
        public float getBabySpawnProbability() {
            return 0.5f;
        }
    }
}
