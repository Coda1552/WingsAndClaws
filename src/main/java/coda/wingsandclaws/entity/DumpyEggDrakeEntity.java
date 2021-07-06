package coda.wingsandclaws.entity;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import coda.wingsandclaws.init.WingsSounds;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.init.WingsItems;
import coda.wingsandclaws.util.LerpFloat;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import coda.wingsandclaws.entity.util.TameableDragonEntity.Gender;

public class DumpyEggDrakeEntity extends TameableDragonEntity {
    private static final DataParameter<Byte> BANDANA_COLOR = EntityDataManager.defineId(DumpyEggDrakeEntity.class, DataSerializers.BYTE);
    private static final EntitySize SLEEPING_SIZE = EntitySize.scalable(1.2f, 0.5f);
    private final AtomicReference<ItemEntity> target = new AtomicReference<>();
    private int alarmedTimer;
    private int attackCooldown;
    private Vector3d oldPos;
    private PlayerEntity closestPlayer;
    public LerpFloat sleepTimer = new LerpFloat().setLimit(0, 1);

    public DumpyEggDrakeEntity(EntityType<? extends DumpyEggDrakeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new SwimGoal(this));
        this.goalSelector.addGoal(4, new SitGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(6, new PanicGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && getTarget() == null;
            }
        });
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(9, new TemptGoal(this, 0.8, false, Ingredient.of(Items.EGG, Items.DRAGON_EGG)) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isOrderedToSit() && target.get() == null;
            }
        });
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 15, 1) {{
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

            @Override
            public boolean canUse() {
                boolean execute = isBaby() && !isOrderedToSit() && super.canUse();
                if (execute && distanceToSqr(lookAt) >= 9) {
                    getNavigation().moveTo(lookAt, 0.6);
                }
                return execute;
            }

            @Override
            public void tick() {
                closestPlayer = (PlayerEntity) lookAt;
                if (distanceToSqr(lookAt) < 9) {
                    getNavigation().stop();
                    getLookControl().setLookAt(this.lookAt, (float) (20 - getMaxHeadYRot()), (float) getMaxHeadXRot());
                } else if (getNavigation().isDone()) {
                    getNavigation().moveTo(lookAt, 0.6);
                }
            }
        });

        this.goalSelector.addGoal(11, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(12, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(13, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> entity == getTarget()));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<DumpyEggDrakeEntity>(this, DumpyEggDrakeEntity.class, 10, true, false, entity -> ((DumpyEggDrakeEntity) entity).getGender() == Gender.MALE)
        {
            @Override
            public boolean canUse() {
                return getGender() == Gender.MALE && super.canUse();
            }
        });
    }

    public static AttributeModifierMap.MutableAttribute registerDEDAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 20).add(Attributes.ATTACK_DAMAGE, 2).add(Attributes.ATTACK_KNOCKBACK, 1).add(Attributes.ATTACK_KNOCKBACK, 1);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BANDANA_COLOR, (byte) DyeColor.WHITE.getId());
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        return isSleeping() ? SLEEPING_SIZE : super.getDimensions(poseIn);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return WingsSounds.DED_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.DED_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.DED_DEATH.get();
    }

    @Override
    public boolean isSilent() {
        return isSleeping() || super.isSilent();
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(isTame() ? 40 : 20);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity && (!(source.getEntity() instanceof PlayerEntity) || (!isOwnedBy((LivingEntity) source.getEntity()) && !((PlayerEntity) source.getEntity()).abilities.instabuild))) {
            Predicate<DumpyEggDrakeEntity> canAttack = entity -> !entity.isBaby() && entity.getGender() == Gender.MALE && !entity.isOwnedBy((LivingEntity) source.getEntity());
            if (canAttack.test(this)) {
                setTarget((LivingEntity) source.getEntity());
            }
            level.getEntitiesOfClass(getClass(), getBoundingBox().inflate(31)).stream().filter(canAttack).forEach(e -> e.setTarget((LivingEntity) source.getEntity()));
        }

        if (isSleeping()) oldPos = position();
        alarmedTimer = 200;
        return super.hurt(source, amount);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && isTame() && stack.getItem() instanceof DyeItem) {
            setBandanaColor(((DyeItem) stack.getItem()).getDyeColor());
            if (!player.abilities.instabuild) stack.shrink(1);
            return ActionResultType.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.EGG;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (oldPos != null) {
            absMoveTo(oldPos.x, oldPos.y, oldPos.z, 0, 0);
            oldPos = null;
        }
        if (!isSleeping()) {
            if (isBaby() && closestPlayer != null) {
                double x = closestPlayer.getX() - getX();
                double z = closestPlayer.getZ() - getZ();

                yRot = (float) Math.toDegrees(Math.atan2(z, x) - Math.PI / 2);
                yBodyRot = yRot;
            }
            LivingEntity attackTarget = getTarget();
            if (attackTarget != null && attackTarget.isAlive() && attackTarget instanceof PlayerEntity && !((PlayerEntity) attackTarget).abilities.instabuild) {
                getNavigation().moveTo(attackTarget, 1.2);
                if (attackCooldown == 0 && distanceToSqr(attackTarget) < 4) {
                    doHurtTarget(attackTarget);
                    attackCooldown = 20;
                }
            } else if (attackCooldown > 0) {
                getNavigation().stop();
                attackCooldown = 0;
                setTarget(null);
            }
            if (attackCooldown-- <= 0) attackCooldown = 0;
            ItemEntity i = target.get();
            if (i == null) {
                level.getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(15)).stream().filter(e -> isFood(e.getItem())).findAny().ifPresent(item -> {
                    getNavigation().moveTo(item, 1);
                    target.set(item);
                });
            } else {
                if (!i.isAlive()) {
                    target.set(null);
                    return;
                }
                if (distanceToSqr(i) < 4) {
                    heal(i.getItem().getCount() * 4);
                    i.remove();
                    target.set(null);
                    getNavigation().stop();
                    if (this.isTame()) {
                        if (this.getAge() == 0 && this.canBreed()) this.setInLove((PlayerEntity) getOwner());
                        else if (this.isBaby()) this.ageUp((int) ((float) (-this.getAge() / 20) * 0.1F), true);
                    } else if (isBaby()) {
                        if (!this.level.isClientSide) {
                            UUID id = i.getThrower();
                            if (id != null) {
                                PlayerEntity player = level.getPlayerByUUID(id);
                                if (player != null) {
                                    if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                                        this.tame(player);
                                        this.navigation.stop();
                                        this.setTarget(null);
                                        this.setHealth(40);
                                        this.level.broadcastEntityEvent(this, (byte) 7);
                                    } else {
                                        this.level.broadcastEntityEvent(this, (byte) 6);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    getNavigation().moveTo(target.get(), 1.2);
                }
            }
            if (alarmedTimer-- <= 0) alarmedTimer = 0;
        } else this.travel(new Vector3d(this.xxa, this.yya, this.zza));
        sleepTimer.add(isSleeping()? 0.085f : -0.085f);
    }

    @Override
    public boolean isSleeping() {
        return alarmedTimer == 0 && level.getDayTime() > 13000 && level.getDayTime() < 23000;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putByte("Color", (byte) this.getBandanaColor().getId());
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        this.setBandanaColor(DyeColor.byId(compound.getByte("Color")));
        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void onLeashDistance(float distance) {
        super.onLeashDistance(distance);
        if (!level.isClientSide && distance >= 5) {
            if (isSleeping()) {
                oldPos = position();
                alarmedTimer = 200;
            }
            dropLeash(true, true);
        }
    }

    @Override
    public ItemStack getEgg() {
        return new ItemStack(WingsItems.DUMPY_EGG_DRAKE_EGG.get());
    }

    public DyeColor getBandanaColor() {
        return DyeColor.byId(this.entityData.get(BANDANA_COLOR));
    }

    private void setBandanaColor(DyeColor color) {
        this.entityData.set(BANDANA_COLOR, (byte) color.getId());
    }
}
