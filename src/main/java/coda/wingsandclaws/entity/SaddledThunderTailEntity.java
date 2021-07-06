package coda.wingsandclaws.entity;

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
import coda.wingsandclaws.entity.util.TameableDragonEntity.Gender;
import net.minecraft.entity.AgeableEntity.AgeableData;

public class SaddledThunderTailEntity extends TameableDragonEntity {
    private static final EntitySize SLEEPING_SIZE = EntitySize.scalable(1.2f, 0.5f);
    private int alarmedTimer;
    private Vector3d oldPos;

    public SaddledThunderTailEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return isSleeping() ? SLEEPING_SIZE : super.getDimensions(poseIn);
    }

    public static AttributeModifierMap.MutableAttribute registerSTTAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.MAX_HEALTH, 150).add(Attributes.ATTACK_DAMAGE, 3);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        boolean bullSpawn;
        if (spawnDataIn instanceof STTData) {
            bullSpawn = ((STTData) spawnDataIn).canMaleSpawn();
        } else {
            bullSpawn = random.nextBoolean();
            spawnDataIn = new STTData();
        }

        AgeableData ageableData = (AgeableData) spawnDataIn;

        if (ageableData.getGroupSize() > 2) {
            this.setAge(-24000);
        }

        if (bullSpawn) {
            ((STTData) spawnDataIn).setCanMaleSpawn(false);
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void setRandomGender(ILivingEntityData spawnDataIn) {
        if (spawnDataIn instanceof STTData) {
            this.setGender(((STTData) spawnDataIn).canMaleSpawn() ? Gender.FEMALE : Gender.MALE);
        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
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
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200);
            this.setHealth(150);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(150);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity && (!(source.getEntity() instanceof PlayerEntity) || (!isOwnedBy((LivingEntity) source.getEntity()) && !((PlayerEntity) source.getEntity()).abilities.instabuild))) {
            if (!isBaby() && getGender() == Gender.MALE && !isOwnedBy((LivingEntity) source.getEntity()))
                setTarget((LivingEntity) source.getEntity());
        }

        if (isSleeping()) {
            oldPos = position();
            alarmedTimer = 200;
        }
        return super.hurt(source, amount);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (handleSpawnEgg(player, stack)) return ActionResultType.SUCCESS;

        if (isBaby() && !isTame() && isFood(stack)) {
            this.usePlayerItem(player, stack);
            if (random.nextInt(20) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setHealth(150);
                this.level.broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level.broadcastEntityEvent(this, (byte) 6);
            }
            return ActionResultType.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.PUMPKIN_PIE; //todo check if this is the right item
    }

    @Override
    public void aiStep() {
        if (oldPos != null) {
            setPos(oldPos.x, oldPos.y, oldPos.z);
            oldPos = null;
        }
        if (!isSleeping()) {
            if (!level.isClientSide) {
                if (isTame()) {
                    if (!isOrderedToSit()) {
                        LivingEntity owner = getOwner();
                        if (owner != null) {
                            getNavigation().moveTo(owner, 0.2);
                            if (onGround) {
                                double x = owner.getX() - getX();
                                double z = owner.getZ() - getZ();
                                setDeltaMovement(MathHelper.clamp(x, -0.2, 0.2), 0, MathHelper.clamp(z, -0.2, 0.2));

                                yRot = (float) Math.toDegrees(Math.atan2(z, x) - Math.PI / 2);
                                yBodyRot = yRot;
                            }
                        }
                    }
                }
                if (alarmedTimer-- <= 0) alarmedTimer = 0;
            }
            super.aiStep();
        } else this.travel(new Vector3d(this.xxa, this.yya, this.zza));
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public boolean isSleeping() {
        return alarmedTimer == 0 && level.getDayTime() > 13000 && level.getDayTime() < 1000;
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
        public float getBabySpawnChance() {
            return 0.5f;
        }
    }
}
