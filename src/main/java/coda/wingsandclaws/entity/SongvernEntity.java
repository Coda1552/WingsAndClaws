package coda.wingsandclaws.entity;

import coda.wingsandclaws.init.WingsSounds;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.entity.goal.FlyGoal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SongvernEntity extends TameableDragonEntity implements IFlyingAnimal {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(SongvernEntity.class, DataSerializers.INT);
    private static final EntityPredicate CAN_BREED = new EntityPredicate().range(64.0D).allowInvulnerable().allowSameTeam().allowUnseeable();
    private SongvernEntity groupLeader;
    private int groupSize = 1;

    public SongvernEntity(EntityType<? extends SongvernEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new FlyingMovementController(this, 10, true);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
    }

    public static AttributeModifierMap.MutableAttribute registerSongvernAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.FLYING_SPEED, 0.8).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.MAX_HEALTH, 10);
    }

    @Override
    protected PathNavigator createNavigation(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(true);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new BreedGoal(this, 1) {
            @Override
            public boolean canUse() {
                return this.animal.isInLove() && (this.partner = this.getNearbyMate()) != null;
            }

            @Nullable
            private AnimalEntity getNearbyMate() {
                List<AnimalEntity> list = this.level.getNearbyEntities(SongvernEntity.class, CAN_BREED, this.animal, this.animal.getBoundingBox().inflate(64.0D));
                double d0 = Double.MAX_VALUE;
                AnimalEntity animalentity = null;

                for (AnimalEntity animalentity1 : list) {
                    if (this.animal.canMate(animalentity1) && this.animal.distanceToSqr(animalentity1) < d0) {
                        animalentity = animalentity1;
                        d0 = this.animal.distanceToSqr(animalentity1);
                    }
                }

                return animalentity;
            }
        });
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new FlyGoal<>(this, null));
        this.goalSelector.addGoal(5, new FlockGoal(this));
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, OcelotEntity.class, 6, 1, 1.2));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.WHEAT_SEEDS;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if (spawnDataIn == null) {
            spawnDataIn = new GroupData(this, random.nextInt(15));
        } else {
            this.setGroupLeaader(((GroupData)spawnDataIn).groupLeader);
        }

        this.setVariant(((GroupData) spawnDataIn).variant);

        return spawnDataIn;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == DamageSource.IN_WALL && !level.getBlockState(blockPosition()).canOcclude()) {
            setDeltaMovement(getDeltaMovement().add(0, -0.05, 0));
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        setVariant(compound.getInt("Variant"));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return WingsSounds.SONGVERN_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.SONGVERN_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.SONGVERN_DEATH.get();
    }

    public void tick() {
        super.tick();
        if (isOrderedToSit()) setDeltaMovement(getDeltaMovement().add(0, -0.05, 0));
        else {
            if (this.isGroupLeader() && this.level.random.nextInt(200) == 1) {
                List<SongvernEntity> list = this.level.getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
                if (list.size() <= 1) {
                    this.groupSize = 1;
                }
            }
        }
    }

    public boolean isFlying() {
        return !this.onGround && !level.getBlockState(new BlockPos(getX() + 0.5, getY() - 0.5, getZ() + 0.5)).canOcclude();
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    public boolean isPushable() {
        return true;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (isTame()) {
                if (player.isShiftKeyDown()) {
                    CompoundNBT compoundnbt = new CompoundNBT();
                    compoundnbt.putString("id", this.getEncodeId());
                    this.saveWithoutId(compoundnbt);
                    if (player.getShoulderEntityLeft().isEmpty() && player.setEntityOnShoulder(compoundnbt)) {
                        this.remove();
                    }
                } else {
                    this.navigation.stop();
                    this.setInSittingPose(true);
                }
                return ActionResultType.SUCCESS;
            } else if (stack.getItem() == Items.HONEYCOMB) {
                if (!player.abilities.instabuild) stack.shrink(1);
                this.tame(player);
                this.setTarget(null);
                this.level.broadcastEntityEvent(this, (byte) 7);
                return ActionResultType.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    public void setGroup(Stream<SongvernEntity> stream) {
        stream.limit(this.getMaxSpawnClusterSize() - this.groupSize).filter(entity -> entity != this).forEach(entity -> entity.setGroupLeaader(this));
    }

    public SongvernEntity setGroupLeaader(SongvernEntity groupLeaderIn) {
        this.groupLeader = groupLeaderIn;
        groupLeaderIn.increaseGroupSize();
        return groupLeaderIn;
    }

    private void increaseGroupSize() {
        ++this.groupSize;
    }

    public boolean hasGroupLeader() {
        return this.groupLeader != null && this.groupLeader.isAlive();
    }

    public void leaveGroup() {
        this.groupLeader.decreaseGroupSize();
        this.groupLeader = null;
    }

    private void decreaseGroupSize() {
        --this.groupSize;
    }

    public boolean canGroupGrow() {
        return this.isGroupLeader() && this.groupSize < this.getMaxSpawnClusterSize();
    }

    public boolean isGroupLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.distanceToSqr(this.groupLeader) <= 121.0D;
    }

    public void moveToGroupLeader() {
        if (this.hasGroupLeader()) {
            this.getNavigation().moveTo(this.groupLeader, 1.0D);
        }
    }

    private static class GroupData implements ILivingEntityData {
        public final SongvernEntity groupLeader;
        public final int variant;

        private GroupData(SongvernEntity groupLeaderIn, int variant) {
            this.groupLeader = groupLeaderIn;
            this.variant = variant;
        }
    }

    private static class FlockGoal extends Goal {
        private final SongvernEntity taskOwner;
        private int navigateTimer;
        private int cooldown;

        public FlockGoal(SongvernEntity taskOwnerIn) {
            this.taskOwner = taskOwnerIn;
            this.cooldown = this.getCooldown(taskOwnerIn);
        }

        protected int getCooldown(SongvernEntity taskOwnerIn) {
            return 200 + taskOwnerIn.getRandom().nextInt(200) % 20;
        }

        public boolean canUse() {
            if (this.taskOwner.isGroupLeader()) {
                return false;
            } else if (this.taskOwner.hasGroupLeader()) {
                return true;
            } else if (this.cooldown > 0) {
                --this.cooldown;
                return false;
            } else {
                this.cooldown = this.getCooldown(this.taskOwner);
                Predicate<SongvernEntity> predicate = (entity) -> entity.canGroupGrow() || !entity.hasGroupLeader();
                List<SongvernEntity> list = this.taskOwner.level.getEntitiesOfClass(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
                SongvernEntity songvernEntity = list.stream().filter(SongvernEntity::canGroupGrow).findAny().orElse(this.taskOwner);
                songvernEntity.setGroup(list.stream().filter(entity -> !entity.hasGroupLeader()));
                return this.taskOwner.hasGroupLeader();
            }
        }

        public boolean canContinueToUse() {
            return this.taskOwner.hasGroupLeader() && this.taskOwner.inRangeOfGroupLeader();
        }

        public void start() {
            this.navigateTimer = 0;
        }

        public void stop() {
            this.taskOwner.leaveGroup();
        }

        public void tick() {
            if (--this.navigateTimer <= 0) {
                this.navigateTimer = 10;
                this.taskOwner.moveToGroupLeader();
            }
        }
    }
}
