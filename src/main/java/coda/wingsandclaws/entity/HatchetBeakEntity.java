package coda.wingsandclaws.entity;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import coda.wingsandclaws.client.WingsKeyBindings;
import coda.wingsandclaws.init.WingsSounds;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.entity.goal.HatchetBeakWanderGoal;
import coda.wingsandclaws.resources.TamePointsManager;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.ai.controller.MovementController;

public class HatchetBeakEntity extends TameableDragonEntity implements IFlyingAnimal {
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.defineId(HatchetBeakEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> FLY_TIMER = EntityDataManager.defineId(HatchetBeakEntity.class, DataSerializers.INT);
    private final Map<UUID, AtomicInteger> players = new HashMap<>();
    public int attackTimer;
    public Vector3d callerPosition;
    private boolean shotDown;
    public float prevTilt;
    public float tilt;

    public HatchetBeakEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
        maxUpStep = 1;
        moveControl = new MovementController(this, 10, true);
        setNoGravity(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1, true)
        {
            @Override
            protected double getAttackReachSqr(LivingEntity attackTarget) {
                return 9;
            }
        });
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1, 40));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 15, 1));
        this.goalSelector.addGoal(4, new HatchetBeakWanderGoal(this));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SADDLED, false);
        this.entityData.define(FLY_TIMER, 0);
    }

    @Override
    protected PathNavigator createNavigation(World worldIn) {
        return new FlyingPathNavigator(this, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute registerHBAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 40).add(Attributes.FLYING_SPEED, 0.5).add(Attributes.ATTACK_DAMAGE, 4);
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tamed ? 60 : 40);
        setHealth(getMaxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return shotDown && super.causeFallDamage(distance, damageMultiplier);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if (shotDown) super.checkFallDamage(y, onGroundIn, state, pos);
    }

    public boolean isFlying() {
        return level.isEmptyBlock(new BlockPos(getX(), getY() - 0.01, getZ()));
    }

    public boolean hasSaddle() {
        return entityData.get(SADDLED);
    }

    public void setFlyTimer(int timer) {
        entityData.set(FLY_TIMER, timer);
    }

    public int getFlyTimer() {
        return entityData.get(FLY_TIMER);
    }

    @Override
    public boolean isSleeping() {
        return !isFlying() && shouldSleep();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        Vector3d vec3d = this.getDeltaMovement();
        if (isFlying() && vec3d.y < 0.0D) {
            this.setDeltaMovement(vec3d.multiply(1.0D, 0.6D, 1.0D));
        }

        prevTilt = tilt;
        if (isFlying()) {
            final float v = MathHelper.degreesDifference(yRot, yRotO);
            if (Math.abs(v) > 1) {
                if (Math.abs(tilt) < 25) {
                    tilt += Math.signum(v);
                }
            } else {
                if (Math.abs(tilt) > 0) {
                    final float tiltSign = Math.signum(tilt);
                    tilt -= tiltSign * 3;
                    if (tilt * tiltSign < 0) {
                        tilt = 0;
                    }
                }
            }
        } else {
            tilt = 0;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.FERMENTED_SPIDER_EYE;
    }

    @Override
    public void travel(Vector3d travelVector) {
        if (this.isAlive()) {
            setShotDown(shotDown && !onGround);
            if (this.isVehicle() && this.canBeControlledByRider() && hasSaddle()) {
                LivingEntity passenger = (LivingEntity) this.getControllingPassenger();
                yRotO = yRot;
                yRot += passenger.xxa * -3.35f;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yBodyRot;

                float f1;
                if (isFlying()) {
                    f1 = Math.max(1.2f, zza + passenger.zza * 6 + 3);
                    if (passenger.zza < 0) {
                        f1 += passenger.zza;
                    }
                } else {
                    f1 = passenger.zza * 0.5F;
                    if (f1 <= 0.0F) {
                        f1 *= 0.25F;
                    }
                }

                if (getFlyTimer() > 0) {
                    setFlyTimer(getFlyTimer() - 1);
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    float verticalMotion = 0f;
                    this.setSpeed((float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                    if (level.isClientSide) {
                        verticalMotion = checkFlight();
                        if (verticalMotion <= 0) {
                            setDeltaMovement(getDeltaMovement().add(0, verticalMotion / 22.5f, 0));
                        } else if (!isFlying() && verticalMotion > 0 && getFlyTimer() == 0) {
                            setFlyTimer(10);
                            verticalMotion = 0;
                        }
                    }
                    super.travel(new Vector3d(0f, yya + verticalMotion, f1));
                } else if (passenger instanceof PlayerEntity) {
                    this.setDeltaMovement(Vector3d.ZERO);
                }
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(travelVector);
            }
        }
    }

    @Override
    public void positionRider(Entity passenger) {
        if (hasPassenger(passenger)) {
            final double rotation = Math.toRadians(MathHelper.wrapDegrees(yRot - 90));
            final float offset = tilt / 25;
            passenger.setPos(getX() + Math.sin(-rotation) * offset, getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), getZ() + Math.cos(rotation) * offset);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 0.15;
    }

    @OnlyIn(Dist.CLIENT)
    private float checkFlight() {
        if (Minecraft.getInstance().options.keyJump.isDown()) {
            return 2f;
        } else if (WingsKeyBindings.HATCHET_BEAK_DESCENT.isDown()) {
            return -3f;
        }

        return 0f;
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!isBaby() && isTame() && isOwnedBy(player)) {
            if (stack.getItem() == Items.SADDLE && !hasSaddle()) {
                if (!player.abilities.instabuild) {
                    stack.shrink(1);
                }
                this.entityData.set(SADDLED, true);
                return ActionResultType.SUCCESS;
            } else if (hasSaddle()) {
                if (stack.getItem() == Items.SHEARS) {
                    this.entityData.set(SADDLED, false);
                    spawnAtLocation(new ItemStack(Items.SADDLE));
                } else {
                    player.startRiding(this);
                    this.navigation.stop();
                }
                return ActionResultType.SUCCESS;
            }
        } else {
            int points = TamePointsManager.INSTANCE.getPoints(getType(), stack.getItem());
            if (points > 0) {
                AtomicInteger playerPoints = players.computeIfAbsent(player.getUUID(), k -> new AtomicInteger());
                playerPoints.set(playerPoints.get() + points);
                if (!player.abilities.instabuild) stack.shrink(1);
                if (playerPoints.get() >= 150 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setHealth(90.0F);
                    players.clear();
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isFlying() && source.isProjectile() && !source.getMsgId().equals("thrown")) {
            setShotDown(true);
        }
        if (level.getDifficulty() != Difficulty.PEACEFUL && source.getEntity() instanceof LivingEntity) {
            setTarget((LivingEntity) source.getEntity());
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return performAttack(entity);
    }

    public boolean performAttack(Entity entity) {
        if (!isFlying()) {
            attackTimer = 15;
            return false;
        }
        return super.doHurtTarget(entity);
    }

    /*    @Override
    public ItemStack getEgg() {
        return new ItemStack(WingsItems.HATCHET_BEAK_EGG);
    }*/

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Saddle", this.entityData.get(SADDLED));
        if (!players.isEmpty()) {
            ListNBT list = new ListNBT();
            for (Map.Entry<UUID, AtomicInteger> entry : players.entrySet()) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putUUID("UUID", entry.getKey());
                nbt.putInt("Value", entry.getValue().get());
                list.add(nbt);
            }
            compound.put("Players", list);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(SADDLED, compound.getBoolean("Saddle"));
        if (compound.contains("Players")) {
            ListNBT list = compound.getList("Players", 10);
            for (INBT entry : list) {
                CompoundNBT nbt = (CompoundNBT) entry;
                players.put(nbt.getUUID("UUID"), new AtomicInteger(nbt.getInt("Values")));
            }
        }
    }

    public void setShotDown(boolean shotDown) {
        this.shotDown = shotDown;
        setNoGravity(!shotDown);
    }

    public boolean isShotDown() {
        return shotDown;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return WingsSounds.HB_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.HB_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.HB_HURT.get();
    }

    private static class MovementController extends net.minecraft.entity.ai.controller.MovementController {
        private final int maxPitchChange;
        private final boolean noGravity;

        public MovementController(MobEntity mob, int maxPitchChange, boolean noGravity) {
            super(mob);
            this.maxPitchChange = maxPitchChange;
            this.noGravity = noGravity;
        }

        public void tick() {
            if (this.operation == MovementController.Action.MOVE_TO) {
                this.operation = MovementController.Action.WAIT;
                this.mob.setNoGravity(true);
                double d0 = this.wantedX - this.mob.getX();
                double d1 = this.wantedY - this.mob.getY();
                double d2 = this.wantedZ - this.mob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double)2.5000003E-7F) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.mob.yRot = this.rotlerp(this.mob.yRot, f, 90.0F);
                float f1;
                if (this.mob.isOnGround()) {
                    f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else {
                    f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED)) * 33;
                }

                this.mob.setSpeed(f1);
                double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                float f2 = (float)(-(MathHelper.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
                this.mob.xRot = this.rotlerp(this.mob.xRot, f2, (float)this.maxPitchChange);
                this.mob.setYya(d1 > 0.0D ? f1 : -f1);
            } else {
                if (!this.noGravity) {
                    this.mob.setNoGravity(false);
                }

                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }
        }
    }
}
