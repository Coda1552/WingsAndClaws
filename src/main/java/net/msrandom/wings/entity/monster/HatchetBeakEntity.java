package net.msrandom.wings.entity.monster;

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
import net.msrandom.wings.client.WingsKeyBindings;
import net.msrandom.wings.client.WingsSounds;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.goal.HatchetBeakWanderGoal;
import net.msrandom.wings.resources.TamePointsManager;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class HatchetBeakEntity extends TameableDragonEntity implements IFlyingAnimal {
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(HatchetBeakEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> FLY_TIMER = EntityDataManager.createKey(HatchetBeakEntity.class, DataSerializers.VARINT);
    private final Map<UUID, AtomicInteger> players = new HashMap<>();
    public int ticksAfloat;
    public int attackTimer;
    public Vector3d callerPosition;

    public HatchetBeakEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
        moveController = new MovementController(this, 10, true);
        setNoGravity(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1, 40));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 15, 1));
        this.goalSelector.addGoal(4, new HatchetBeakWanderGoal(this));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SADDLED, false);
        this.dataManager.register(FLY_TIMER, 0);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return new FlyingPathNavigator(this, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute registerHBAttributes() {
        return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.MAX_HEALTH, 60).createMutableAttribute(Attributes.FLYING_SPEED, 0.5).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tamed ? 90 : 60);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8);
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean isFlying() {
        return world.isAirBlock(new BlockPos(getPosX(), getPosY() - 0.01, getPosZ()));
    }

    public boolean hasSaddle() {
        return dataManager.get(SADDLED);
    }

    public void setFlyTimer(int timer) {
        dataManager.set(FLY_TIMER, timer);
    }

    public int getFlyTimer() {
        return dataManager.get(FLY_TIMER);
    }

    @Override
    public boolean isSleeping() {
        return !isFlying() && shouldSleep();
    }

    @Override
    public void livingTick() {
        super.livingTick();

        Vector3d vec3d = this.getMotion();
        if (isFlying() && vec3d.y < 0.0D) {
            this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.FERMENTED_SPIDER_EYE;
    }

    @Override
    public void travel(Vector3d positionIn) {
        if (this.isAlive()) {
            if (this.isBeingRidden() && this.canBeSteered() && hasSaddle()) {
                LivingEntity passenger = (LivingEntity) this.getControllingPassenger();
                rotationYaw += passenger.moveStrafing * -3.35f;
                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch = passenger.rotationPitch * 0.5F;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.renderYawOffset;

                float f1;
                if (isFlying()) {
                    f1 = Math.max(1.2f, moveForward + passenger.moveForward * 6 + 3);
                } else {
                    f1 = passenger.moveForward * 0.5F;
                    if (f1 <= 0.0F) {
                        f1 *= 0.25F;
                    }
                }

                if (getFlyTimer() > 0) {
                    setFlyTimer(getFlyTimer() - 1);
                }

                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
                if (this.canPassengerSteer()) {
                    float verticalMotion = 0f;
                    this.setAIMoveSpeed((float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                    if (world.isRemote) {
                        verticalMotion = checkFlight();
                        if (verticalMotion <= 0) {
                            setMotion(getMotion().add(0, verticalMotion / 22.5f, 0));
                        } else if (!isFlying() && verticalMotion > 0 && getFlyTimer() == 0) {
                            setFlyTimer(10);
                            verticalMotion = 0;
                        }
                    }
                    super.travel(new Vector3d(0f, moveVertical + verticalMotion, f1));
                } else if (passenger instanceof PlayerEntity) {
                    this.setMotion(Vector3d.ZERO);
                }
            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(positionIn);
            }
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (isPassenger(passenger)) {
            passenger.setPosition(getPosX() + 0.85 * Math.sin(Math.toRadians(-rotationYaw)), getPosY() + this.getMountedYOffset() + passenger.getYOffset(), getPosZ() + 0.85 * Math.cos(Math.toRadians(rotationYaw)));
        }
    }

    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() + 0.15;
    }

    @OnlyIn(Dist.CLIENT)
    private float checkFlight() {
        if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
            return 2f;
        } else if (WingsKeyBindings.HATCHET_BEAK_DESCENT.isKeyDown()) {
            return -3f;
        }

        return 0f;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!isChild() && isTamed() && isOwner(player)) {
            if (stack.getItem() == Items.SADDLE && !hasSaddle()) {
                if (!player.abilities.isCreativeMode) {
                    stack.shrink(1);
                }
                this.dataManager.set(SADDLED, true);
                return ActionResultType.SUCCESS;
            }
            else if (hasSaddle()) {
                if (stack.getItem() == Items.SHEARS) {
                    this.dataManager.set(SADDLED, false);
                    entityDropItem(new ItemStack(Items.SADDLE));
                } else {
                    player.startRiding(this);
                }
                return ActionResultType.SUCCESS;
            }
        } else {
            int points = TamePointsManager.INSTANCE.getPoints(getType(), stack.getItem());
            if (points > 0) {
                AtomicInteger playerPoints = players.computeIfAbsent(player.getUniqueID(), k -> new AtomicInteger());
                playerPoints.set(playerPoints.get() + points);
                if (!player.abilities.isCreativeMode) stack.shrink(1);
                if (playerPoints.get() >= 150 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.setTamedBy(player);
                    this.navigator.clearPath();
                    this.setAttackTarget(null);
                    this.setHealth(90.0F);
                    players.clear();
                    this.world.setEntityState(this, (byte) 7);
                } else {
                    this.world.setEntityState(this, (byte) 6);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return super.func_230254_b_(player, hand);
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (world.getDifficulty() != Difficulty.PEACEFUL && source.getTrueSource() instanceof LivingEntity) {
            setAttackTarget((LivingEntity) source.getTrueSource());
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return performAttack(entity);
    }

    public boolean performAttack(Entity entity) {
        if (!isFlying()) {
            attackTimer = 15;
            return false;
        }
        return super.attackEntityAsMob(entity);
    }

    /*    @Override
    public ItemStack getEgg() {
        return new ItemStack(WingsItems.HATCHET_BEAK_EGG);
    }*/

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Saddle", this.dataManager.get(SADDLED));
        if (!players.isEmpty()) {
            ListNBT list = new ListNBT();
            for (Map.Entry<UUID, AtomicInteger> entry : players.entrySet()) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putUniqueId("UUID", entry.getKey());
                nbt.putInt("Value", entry.getValue().get());
                list.add(nbt);
            }
            compound.put("Players", list);
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(SADDLED, compound.getBoolean("Saddle"));
        if (compound.contains("Players")) {
            ListNBT list = compound.getList("Players", 10);
            for (INBT entry : list) {
                CompoundNBT nbt = (CompoundNBT) entry;
                players.put(nbt.getUniqueId("UUID"), new AtomicInteger(nbt.getInt("Values")));
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return WingsSounds.HB_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.HB_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.HB_HURT;
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
            if (this.action == net.minecraft.entity.ai.controller.MovementController.Action.MOVE_TO) {
                this.action = net.minecraft.entity.ai.controller.MovementController.Action.WAIT;
                this.mob.setNoGravity(true);
                double d0 = this.posX - this.mob.getPosX();
                double d1 = this.posY - this.mob.getPosY();
                double d2 = this.posZ - this.mob.getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double)2.5000003E-7F) {
                    this.mob.setMoveVertical(0.0F);
                    this.mob.setMoveForward(0.0F);
                    return;
                }

                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f, 90.0F);
                float f1;
                if (this.mob.isOnGround()) {
                    f1 = (float)(this.speed * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else {
                    f1 = (float)(this.speed * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
                }

                this.mob.setAIMoveSpeed(f1);
                double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                float f2 = (float)(-(MathHelper.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
                this.mob.rotationPitch = this.limitAngle(this.mob.rotationPitch, f2, (float)this.maxPitchChange);
                this.mob.setMoveVertical(d1 > 0.0D ? f1 : -f1);
            } else {
                if (!this.noGravity) {
                    this.mob.setNoGravity(false);
                }

                this.mob.setMoveVertical(0.0F);
                this.mob.setMoveForward(0.0F);
            }
        }
    }
}
