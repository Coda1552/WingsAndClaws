package net.msrandom.wings.entity.passive;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.item.WingsItems;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class DumpyEggDrakeEntity extends TameableDragonEntity {
    private static final TrackedData<Byte> BANDANA_COLOR = DataTracker.registerData(DumpyEggDrakeEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final EntityDimensions SLEEPING_SIZE = EntityDimensions.changing(1.2f, 0.5f);
    private final AtomicReference<ItemEntity> target = new AtomicReference<>();
    private int alarmedTimer;
    private int attackCooldown;
    private Vec3d oldPos;
    private PlayerEntity closestPlayer;

    public DumpyEggDrakeEntity(EntityType<? extends DumpyEggDrakeEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new SwimGoal(this));
        this.goalSelector.add(4, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new EscapeDangerGoal(this, 1.0D) {
            @Override
            public boolean canStart() {
                return super.canStart() && getTarget() == null;
            }
        });
        this.goalSelector.add(7, new FollowParentGoal(this, 1.1D));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false) {
            @Override
            public boolean canStart() {
                return super.canStart() && getState() == WanderState.FOLLOW;
            }
        });
        this.goalSelector.add(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D) {
            @Override
            public boolean canStart() {
                return super.canStart() && getState() == WanderState.WANDER;
            }
        });
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 0.8, false, Ingredient.fromItems(Items.EGG, Items.DRAGON_EGG)) {
            @Override
            public boolean canStart() {
                return super.canStart() && target.get() == null && getState() == WanderState.WANDER;
            }
        });
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 15, 1) {
            @Override
            public boolean canStart() {
                boolean execute = isBaby() && getState() == WanderState.WANDER && super.canStart();
                if (execute && squaredDistanceTo(target) >= 9) {
                    getNavigation().startMovingTo(target, 0.6);
                }
                return execute;
            }

            @Override
            public void tick() {
                closestPlayer = (PlayerEntity) target;
                if (squaredDistanceTo(target) < 9) {
                    getNavigation().stop();
                    getLookControl().lookAt(this.target, (float) (20 - getHorizontalFaceSpeed()), (float) getVerticalFaceSpeed());
                } else {
                    getNavigation().startMovingTo(target, 0.6);
                }
            }
        });
        this.goalSelector.add(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> entity == getTarget()));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(isTamed() ? 40 : 20);
        this.getAttributes().registerAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BANDANA_COLOR, (byte) DyeColor.RED.getId());
    }

    @Override
    public EntityDimensions getDimensions(EntityPose poseIn) {
        return isSleeping() ? SLEEPING_SIZE : super.getSize(poseIn);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return WingsSounds.DED_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.DED_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.DED_DEATH;
    }

    @Override
    public boolean isSilent() {
        return isSleeping() || super.isSilent();
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(isTamed() ? 40 : 20);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof LivingEntity && (!(source.getTrueSource() instanceof PlayerEntity) || (!isOwner((LivingEntity) source.getTrueSource()) && !((PlayerEntity) source.getTrueSource()).abilities.creativeMode))) {
            Predicate<DumpyEggDrakeEntity> canAttack = entity -> !entity.isBaby() && entity.getGender() && !entity.isOwner((LivingEntity) source.getTrueSource());
            if (canAttack.test(this)) {
                setTarget((LivingEntity) source.getTrueSource());
            }
            world.getEntitiesWithinAABB(getClass(), getBoundingBox().grow(31)).stream().filter(canAttack).forEach(e -> e.setTarget((LivingEntity) source.getTrueSource()));
        }

        if (isSleeping()) oldPos = getPositionVec();
        alarmedTimer = 200;
        return super.damage(source, amount);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient && isTamed() && stack.getItem() instanceof DyeItem) {
            setBandanaColor(((DyeItem) stack.getItem()).getDyeColor());
            if (!player.abilities.creativeMode) stack.decrement(1);
        }
        if (stack.getItem() instanceof SpawnEggItem && ((SpawnEggItem) stack.getItem()).hasType(stack.getTag(), this.getType())) {
            if (!this.world.isClient) {
                DumpyEggDrakeEntity drake = WingsEntities.DUMPY_EGG_DRAKE.create(world);
                if (drake != null) {
                    drake.setBreedingAge(-24000);
                    drake.setLocationAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                    drake.initialize(world, world.getLocalDifficulty(drake.getBlockPos()), SpawnReason.SPAWN_EGG, null, null);
                    this.world.spawnEntity(drake);
                    if (stack.hasDisplayName()) {
                        drake.setCustomName(stack.getDisplayName());
                    }

                    if (!player.abilities.creativeMode) {
                        stack.decrement(1);
                    }
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.EGG;
    }

    @Override
    public void mobTick() {
        if (oldPos != null) {
            setPositionAndRotation(oldPos.x, oldPos.y, oldPos.z, 0, 0);
            oldPos = null;
        }
        if (!isSleeping()) {
            if (isBaby() && closestPlayer != null) {
                double x = closestPlayer.getX() - getX();
                double z = closestPlayer.getZ() - getZ();

                rotationYaw = (float) Math.toDegrees(Math.atan2(z, x) - Math.PI / 2);
                renderYawOffset = rotationYaw;
            }
            LivingEntity attackTarget = getTarget();
            if (attackTarget != null && attackTarget.isAlive() && attackTarget instanceof PlayerEntity && !((PlayerEntity) attackTarget).abilities.creativeMode) {
                getNavigation().startMovingTo(attackTarget, 1.2);
                if (attackCooldown == 0 && squaredDistanceTo(attackTarget) < 4) {
                    attackEntityAsMob(attackTarget);
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
                world.getEntitiesWithinAABB(ItemEntity.class, getBoundingBox().grow(15)).stream().filter(e -> isBreedingItem(e.getItem())).findAny().ifPresent(item -> {
                    getNavigation().startMovingTo(item, 1);
                    target.set(item);
                });
            } else {
                if (!i.isAlive()) {
                    target.set(null);
                    return;
                }
                if (squaredDistanceTo(i) < 4) {
                    heal(i.getItem().getCount() * 4);
                    i.remove();
                    target.set(null);
                    getNavigation().stop();
                    if (this.isTamed()) {
                        if (this.getGrowingAge() == 0 && this.canBreed()) this.setInLove((PlayerEntity) getOwner());
                        else if (this.isBaby()) this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.1F), true);
                    } else if (isBaby()) {
                        if (!this.world.isClient) {
                            UUID id = i.getThrowerId();
                            if (id != null) {
                                PlayerEntity player = world.getPlayerByUuid(id);
                                if (player != null) {
                                    if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                                        this.setTamedBy(player);
                                        this.navigator.stop();
                                        this.setTarget(null);
                                        this.setHealth(40);
                                        this.playTameEffect(true);
                                        this.world.sendEntityStatus(this, (byte) 7);
                                    } else {
                                        this.playTameEffect(false);
                                        this.world.sendEntityStatus(this, (byte) 6);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    getNavigation().startMovingTo(target.get(), 1.2);
                }
            }
            if (alarmedTimer-- <= 0) alarmedTimer = 0;
            super.mobTick();
        } else this.travel(new Vec3d(this.moveStrafing, this.moveVertical, this.moveForward));
    }

    @Override
    public boolean isSleeping() {
        return alarmedTimer == 0 && world.getDayTime() > 13000 && world.getDayTime() < 23000;
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compound) {
        compound.putByte("Color", (byte) this.getBandanaColor().getId());
        super.writeCustomDataToTag(compound);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound) {
        this.setBandanaColor(DyeColor.byId(compound.getByte("Color")));
        super.readCustomDataFromTag(compound);
    }

    @Override
    protected void onLeashDistance(float distance) {
        super.onLeashDistance(distance);
        if (!world.isClient && distance >= 5) {
            if (isSleeping()) {
                oldPos = getPositionVec();
                alarmedTimer = 200;
            }
            clearLeashed(true, true);
        }
    }

    @Override
    public ItemStack getEgg() {
        return new ItemStack(WingsItems.DUMPY_EGG_DRAKE_EGG);
    }

    public DyeColor getBandanaColor() {
        return DyeColor.byId(this.dataTracker.get(BANDANA_COLOR));
    }

    private void setBandanaColor(DyeColor color) {
        this.dataTracker.set(BANDANA_COLOR, (byte) color.getId());
    }
}
