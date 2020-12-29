package net.msrandom.wings.entity.passive;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
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
import net.msrandom.wings.client.WingsSounds;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.item.WingsItems;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class DumpyEggDrakeEntity extends TameableDragonEntity {
    private static final DataParameter<Byte> BANDANA_COLOR = EntityDataManager.createKey(DumpyEggDrakeEntity.class, DataSerializers.BYTE);
    private static final EntitySize SLEEPING_SIZE = EntitySize.flexible(1.2f, 0.5f);
    private final AtomicReference<ItemEntity> target = new AtomicReference<>();
    private int alarmedTimer;
    private int attackCooldown;
    private Vector3d oldPos;
    private PlayerEntity closestPlayer;

    public DumpyEggDrakeEntity(EntityType<? extends DumpyEggDrakeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new SwimGoal(this));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.0D) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && getAttackTarget() == null;
            }
        });
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && getState() == WanderState.FOLLOW;
            }
        });
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && getState() == WanderState.WANDER;
            }
        });
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 0.8, false, Ingredient.fromItems(Items.EGG, Items.DRAGON_EGG)) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && target.get() == null && getState() == WanderState.WANDER;
            }
        });
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 15, 1) {
            @Override
            public boolean shouldExecute() {
                boolean execute = isChild() && getState() == WanderState.WANDER && super.shouldExecute();
                if (execute && getDistanceSq(closestEntity) >= 9) {
                    getNavigator().tryMoveToEntityLiving(closestEntity, 0.6);
                }
                return execute;
            }

            @Override
            public void tick() {
                closestPlayer = (PlayerEntity) closestEntity;
                if (getDistanceSq(closestEntity) < 9) {
                    getNavigator().clearPath();
                    getLookController().setLookPositionWithEntity(this.closestEntity, (float) (20 - getHorizontalFaceSpeed()), (float) getVerticalFaceSpeed());
                } else if (getNavigator().noPath()) {
                    getNavigator().tryMoveToEntityLiving(closestEntity, 0.6);
                }
            }
        });
        this.goalSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> entity == getAttackTarget()));
    }

    public static AttributeModifierMap.MutableAttribute registerDEDAttributes() {
        return LivingEntity.registerAttributes().createMutableAttribute(Attributes.FOLLOW_RANGE, 16).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.MAX_HEALTH, 20).createMutableAttribute(Attributes.ATTACK_DAMAGE, 2).createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1).createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(BANDANA_COLOR, (byte) DyeColor.RED.getId());
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
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
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(isTamed() ? 40 : 20);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof LivingEntity && (!(source.getTrueSource() instanceof PlayerEntity) || (!isOwner((LivingEntity) source.getTrueSource()) && !((PlayerEntity) source.getTrueSource()).abilities.isCreativeMode))) {
            Predicate<DumpyEggDrakeEntity> canAttack = entity -> !entity.isChild() && entity.getGender() && !entity.isOwner((LivingEntity) source.getTrueSource());
            if (canAttack.test(this)) {
                setAttackTarget((LivingEntity) source.getTrueSource());
            }
            world.getEntitiesWithinAABB(getClass(), getBoundingBox().grow(31)).stream().filter(canAttack).forEach(e -> e.setAttackTarget((LivingEntity) source.getTrueSource()));
        }

        if (isSleeping()) oldPos = getPositionVec();
        alarmedTimer = 200;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && isTamed() && stack.getItem() instanceof DyeItem) {
            setBandanaColor(((DyeItem) stack.getItem()).getDyeColor());
            if (!player.abilities.isCreativeMode) stack.shrink(1);
            return ActionResultType.SUCCESS;
        }
        return super.func_230254_b_(player, hand);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.EGG;
    }

    @Override
    public void livingTick() {
        if (oldPos != null) {
            setPositionAndRotation(oldPos.x, oldPos.y, oldPos.z, 0, 0);
            oldPos = null;
        }
        if (!isSleeping()) {
            if (isChild() && closestPlayer != null) {
                double x = closestPlayer.getPosX() - getPosX();
                double z = closestPlayer.getPosZ() - getPosZ();

                rotationYaw = (float) Math.toDegrees(Math.atan2(z, x) - Math.PI / 2);
                renderYawOffset = rotationYaw;
            }
            LivingEntity attackTarget = getAttackTarget();
            if (attackTarget != null && attackTarget.isAlive() && attackTarget instanceof PlayerEntity && !((PlayerEntity) attackTarget).abilities.isCreativeMode) {
                getNavigator().tryMoveToEntityLiving(attackTarget, 1.2);
                if (attackCooldown == 0 && getDistanceSq(attackTarget) < 4) {
                    attackEntityAsMob(attackTarget);
                    attackCooldown = 20;
                }
            } else if (attackCooldown > 0) {
                getNavigator().clearPath();
                attackCooldown = 0;
                setAttackTarget(null);
            }
            if (attackCooldown-- <= 0) attackCooldown = 0;
            ItemEntity i = target.get();
            if (i == null) {
                world.getEntitiesWithinAABB(ItemEntity.class, getBoundingBox().grow(15)).stream().filter(e -> isBreedingItem(e.getItem())).findAny().ifPresent(item -> {
                    getNavigator().tryMoveToEntityLiving(item, 1);
                    target.set(item);
                });
            } else {
                if (!i.isAlive()) {
                    target.set(null);
                    return;
                }
                if (getDistanceSq(i) < 4) {
                    heal(i.getItem().getCount() * 4);
                    i.remove();
                    target.set(null);
                    getNavigator().clearPath();
                    if (this.isTamed()) {
                        if (this.getGrowingAge() == 0 && this.canBreed()) this.setInLove((PlayerEntity) getOwner());
                        else if (this.isChild()) this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.1F), true);
                    } else if (isChild()) {
                        if (!this.world.isRemote) {
                            UUID id = i.getThrowerId();
                            if (id != null) {
                                PlayerEntity player = world.getPlayerByUuid(id);
                                if (player != null) {
                                    if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                                        this.setTamedBy(player);
                                        this.navigator.clearPath();
                                        this.setAttackTarget(null);
                                        this.setHealth(40);
                                        this.world.setEntityState(this, (byte) 7);
                                    } else {
                                        this.world.setEntityState(this, (byte) 6);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    getNavigator().tryMoveToEntityLiving(target.get(), 1.2);
                }
            }
            if (alarmedTimer-- <= 0) alarmedTimer = 0;
            super.livingTick();
        } else this.travel(new Vector3d(this.moveStrafing, this.moveVertical, this.moveForward));
    }

    @Override
    public boolean isSleeping() {
        return alarmedTimer == 0 && world.getDayTime() > 13000 && world.getDayTime() < 23000;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putByte("Color", (byte) this.getBandanaColor().getId());
        super.writeAdditional(compound);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        this.setBandanaColor(DyeColor.byId(compound.getByte("Color")));
        super.readAdditional(compound);
    }

    @Override
    protected void onLeashDistance(float distance) {
        super.onLeashDistance(distance);
        if (!world.isRemote && distance >= 5) {
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
        return DyeColor.byId(this.dataManager.get(BANDANA_COLOR));
    }

    private void setBandanaColor(DyeColor color) {
        this.dataManager.set(BANDANA_COLOR, (byte) color.getId());
    }
}
