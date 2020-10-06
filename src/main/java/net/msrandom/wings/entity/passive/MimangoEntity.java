package net.msrandom.wings.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.msrandom.wings.WingsSounds;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.goal.MimangoFlyGoal;
import net.msrandom.wings.entity.goal.MimangoHideGoal;

import javax.annotation.Nullable;
import java.util.List;

public class MimangoEntity extends TameableDragonEntity implements IFlyingAnimal {
    private static final EntityPredicate CAN_BREED = new EntityPredicate().setDistance(64.0D).allowInvulnerable().allowFriendlyFire().setLineOfSiteRequired();
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(MimangoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HIDING = EntityDataManager.createKey(MimangoEntity.class, DataSerializers.BOOLEAN);
    //private static final Ingredient TEMPT_ITEM = Ingredient.fromItems(WingsBlocks.MANGO_BUNCH.asItem());

    private MimangoHideGoal hangGoal;

    public MimangoEntity(EntityType<? extends MimangoEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new FlyingMovementController(this, 10, true);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.8);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(true);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.hangGoal = new MimangoHideGoal(this, 5.0D);
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new BreedGoal(this, 1) {
            @Override
            public boolean shouldExecute() {
                return this.animal.isInLove() && (this.targetMate = this.getNearbyMate()) != null;
            }

            @Nullable
            private AnimalEntity getNearbyMate() {
                List<AnimalEntity> list = this.world.getTargettableEntitiesWithinAABB(MimangoEntity.class, CAN_BREED, this.animal, this.animal.getBoundingBox().grow(64.0D));
                double d0 = Double.MAX_VALUE;
                AnimalEntity animalentity = null;

                for (AnimalEntity animalentity1 : list) {
                    if (this.animal.canMateWith(animalentity1) && this.animal.getDistanceSq(animalentity1) < d0) {
                        animalentity = animalentity1;
                        d0 = this.animal.getDistanceSq(animalentity1);
                    }
                }

                return animalentity;
            }
        });
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && getState() == WanderState.FOLLOW;
            }
        });
        this.goalSelector.addGoal(3, hangGoal);
        this.goalSelector.addGoal(4, new MimangoFlyGoal(this));
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, OcelotEntity.class, 6, 1, 1.2));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(HIDING, false);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.COCOA_BEANS;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        setVariant(rand.nextInt(5));
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        this.setHiding(false);
        if (source == DamageSource.IN_WALL && !world.getBlockState(getPosition()).isSolid()) {
            setMotion(getMotion().add(0, -0.05, 0));
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
            playSound(WingsSounds.MIMANGO_HAPPY, getSoundVolume(), getSoundPitch());
            return true;
        }

        if (handleSpawnEgg(player, stack)) return true;

        if (!isTamed() && stack.getItem() == WingsBlocks.MANGO_BUNCH.asItem()) {
            if (rand.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                this.setTamedBy(player);
                this.navigator.clearPath();
                this.goalSelector.removeGoal(hangGoal);
                this.setAttackTarget(null);
                this.setHealth(20.0F);
                this.world.setEntityState(this, (byte) 7);
            } else {
                this.world.setEntityState(this, (byte) 6);
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", getVariant());
        compound.putBoolean("Hiding", isHiding());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setVariant(compound.getInt("Variant"));
        setHiding(compound.getBoolean("Hiding"));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return WingsSounds.MIMANGO_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.MIMANGO_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.MIMANGO_DEATH;
    }

    public boolean isHiding() {
        return this.dataManager.get(HIDING);
    }

    public void setHiding(boolean isHanging) {
        this.dataManager.set(HIDING, isHanging);
    }

    public void tick() {
        super.tick();
        if (getState() == WanderState.STAY) setMotion(getMotion().add(0, -0.05, 0));
        if (this.isHiding()) {
            this.setMotion(Vec3d.ZERO);
            this.setRawPosition(this.getPosX(), (double) MathHelper.floor(this.getPosY()) + 1.0D - (double) this.getHeight(), this.getPosZ());
        }
    }

    public boolean isFlying() {
        return !this.onGround && !world.getBlockState(new BlockPos(getPosX() + 0.5, getPosY() - 0.5, getPosZ() + 0.5)).isSolid();
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    public boolean canBePushed() {
        return true;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }
}
