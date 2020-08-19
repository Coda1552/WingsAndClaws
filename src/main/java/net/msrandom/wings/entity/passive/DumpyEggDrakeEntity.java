package net.msrandom.wings.entity.passive;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.item.WingsItems;

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
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D) {
            @Override
            public boolean canStart() {
                return super.canStart() && getState() == WanderState.WANDER;
            }
        });
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 0.8, false, Ingredient.ofItems(Items.EGG, Items.DRAGON_EGG)) {
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
                    getLookControl().lookAt(this.target, (float) (20 - getBodyYawSpeed()), (float) getLookPitchSpeed());
                } else {
                    getNavigation().startMovingTo(target, 0.6);
                }
            }
        });
        this.targetSelector.add(0, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> entity == getTarget()));
    }

    public static DefaultAttributeContainer.Builder registerDEDAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3).add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BANDANA_COLOR, (byte) DyeColor.RED.getId());
    }

    @Override
    public EntityDimensions getDimensions(EntityPose poseIn) {
        return isSleeping() ? SLEEPING_SIZE : super.getDimensions(poseIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return WingsSounds.DED_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.DED_HURT;
    }

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
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(tamed ? 40 : 20);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        LivingEntity attacker = source.getAttacker() instanceof LivingEntity ? (LivingEntity) source.getAttacker() : null;
        if (attacker != null && (!(attacker instanceof PlayerEntity) || (!isOwner(attacker) && !((PlayerEntity) attacker).abilities.creativeMode))) {
            Predicate<DumpyEggDrakeEntity> canAttack = entity -> !entity.isBaby() && entity.getGender() && !entity.isOwner(attacker);
            if (canAttack.test(this)) {
                setTarget(attacker);
            }
            for (DumpyEggDrakeEntity e : world.getEntities(getClass(), getBoundingBox().expand(31), canAttack)) {
                e.setTarget(attacker);
            }
        }

        if (isSleeping()) oldPos = getPos();
        alarmedTimer = 200;
        return super.damage(source, amount);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient && isTamed() && stack.getItem() instanceof DyeItem) {
            setBandanaColor(((DyeItem) stack.getItem()).getColor());
            if (!player.abilities.creativeMode) stack.decrement(1);
        }
        if (stack.getItem() instanceof SpawnEggItem && ((SpawnEggItem) stack.getItem()).isOfSameEntityType(stack.getTag(), this.getType())) {
            if (!this.world.isClient) {
                DumpyEggDrakeEntity drake = WingsEntities.DUMPY_EGG_DRAKE.create(world);
                if (drake != null) {
                    drake.setBreedingAge(-24000);
                    drake.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                    drake.initialize(world, world.getLocalDifficulty(drake.getBlockPos()), SpawnReason.SPAWN_EGG, null, null);
                    this.world.spawnEntity(drake);
                    if (stack.hasCustomName()) {
                        drake.setCustomName(stack.getName());
                    }

                    if (!player.abilities.creativeMode) {
                        stack.decrement(1);
                    }
                }
            }

            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.EGG;
    }

    @Override
    public void mobTick() {
        if (oldPos != null) {
            updatePositionAndAngles(oldPos.x, oldPos.y, oldPos.z, 0, 0);
            oldPos = null;
        }
        if (!isSleeping()) {
            if (isBaby() && closestPlayer != null) {
                double x = closestPlayer.getX() - getX();
                double z = closestPlayer.getZ() - getZ();

                yaw = (float) Math.toDegrees(Math.atan2(z, x) - Math.PI / 2);
                bodyYaw = yaw;
            }
            LivingEntity attackTarget = getTarget();
            if (attackTarget != null && attackTarget.isAlive() && attackTarget instanceof PlayerEntity && !((PlayerEntity) attackTarget).abilities.creativeMode) {
                getNavigation().startMovingTo(attackTarget, 1.2);
                if (attackCooldown == 0 && squaredDistanceTo(attackTarget) < 4) {
                    tryAttack(attackTarget);
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
                world.getNonSpectatingEntities(ItemEntity.class, getBoundingBox().expand(15)).stream().filter(e -> isBreedingItem(e.getStack())).findAny().ifPresent(item -> {
                    getNavigation().startMovingTo(item, 1);
                    target.set(item);
                });
            } else {
                if (!i.isAlive()) {
                    target.set(null);
                    return;
                }
                if (squaredDistanceTo(i) < 4) {
                    heal(i.getStack().getCount() * 4);
                    i.remove();
                    target.set(null);
                    getNavigation().stop();
                    if (this.isTamed()) {
                        if (this.getBreedingAge() == 0 && this.canEat()) this.lovePlayer((PlayerEntity) getOwner());
                        else if (this.isBaby()) this.growUp((int) ((float) (-this.getBreedingAge() / 20) * 0.1F), true);
                    } else if (isBaby()) {
                        if (!this.world.isClient) {
                            UUID id = i.getThrower();
                            if (id != null) {
                                PlayerEntity player = world.getPlayerByUuid(id);
                                if (player != null) {
                                    if (this.random.nextInt(3) == 0) {
                                        this.setOwner(player);
                                        this.navigation.stop();
                                        this.setTarget(null);
                                        this.setHealth(40);
                                        this.world.sendEntityStatus(this, (byte) 7);
                                    } else {
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
        } else this.travel(new Vec3d(this.sidewaysSpeed, this.upwardSpeed, this.forwardSpeed));
    }

    @Override
    public boolean isSleeping() {
        return alarmedTimer == 0 && world.getTimeOfDay() > 13000 && world.getTimeOfDay() < 23000;
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
    protected void updateForLeashLength(float leashLength) {
        super.updateForLeashLength(leashLength);
        if (!world.isClient && leashLength >= 5) {
            if (isSleeping()) {
                oldPos = getPos();
                alarmedTimer = 200;
            }
            detachLeash(true, true);
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
