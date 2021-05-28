package coda.wingsandclaws.entity.passive;

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
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(SongvernEntity.class, DataSerializers.VARINT);
    private static final EntityPredicate CAN_BREED = new EntityPredicate().setDistance(64.0D).allowInvulnerable().allowFriendlyFire().setLineOfSiteRequired();
    private SongvernEntity groupLeader;
    private int groupSize = 1;

    public SongvernEntity(EntityType<? extends SongvernEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new FlyingMovementController(this, 10, true);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
    }

    public static AttributeModifierMap.MutableAttribute registerSongvernAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.FLYING_SPEED, 0.8).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5).createMutableAttribute(Attributes.MAX_HEALTH, 10);
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
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new BreedGoal(this, 1) {
            @Override
            public boolean shouldExecute() {
                return this.animal.isInLove() && (this.targetMate = this.getNearbyMate()) != null;
            }

            @Nullable
            private AnimalEntity getNearbyMate() {
                List<AnimalEntity> list = this.world.getTargettableEntitiesWithinAABB(SongvernEntity.class, CAN_BREED, this.animal, this.animal.getBoundingBox().grow(64.0D));
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
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new FlyGoal<>(this, null));
        this.goalSelector.addGoal(5, new FlockGoal(this));
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, OcelotEntity.class, 6, 1, 1.2));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, 0);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.WHEAT_SEEDS;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if (spawnDataIn == null) {
            spawnDataIn = new GroupData(this, rand.nextInt(15));
        } else {
            this.setGroupLeaader(((GroupData)spawnDataIn).groupLeader);
        }

        this.setVariant(((GroupData) spawnDataIn).variant);

        return spawnDataIn;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.IN_WALL && !world.getBlockState(getPosition()).isSolid()) {
            setMotion(getMotion().add(0, -0.05, 0));
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", getVariant());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
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
        if (isSitting()) setMotion(getMotion().add(0, -0.05, 0));
        else {
            if (this.isGroupLeader() && this.world.rand.nextInt(200) == 1) {
                List<SongvernEntity> list = this.world.getEntitiesWithinAABB(this.getClass(), this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
                if (list.size() <= 1) {
                    this.groupSize = 1;
                }
            }
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

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            if (isTamed()) {
                if (player.isSneaking()) {
                    CompoundNBT compoundnbt = new CompoundNBT();
                    compoundnbt.putString("id", this.getEntityString());
                    this.writeWithoutTypeId(compoundnbt);
                    if (player.getLeftShoulderEntity().isEmpty() && player.addShoulderEntity(compoundnbt)) {
                        this.remove();
                    }
                } else {
                    this.navigator.clearPath();
                    this.setSleeping(true);
                }
                return ActionResultType.SUCCESS;
            } else if (stack.getItem() == Items.HONEYCOMB) {
                if (!player.abilities.isCreativeMode) stack.shrink(1);
                this.setTamedBy(player);
                this.setAttackTarget(null);
                this.world.setEntityState(this, (byte) 7);
                return ActionResultType.SUCCESS;
            }
        }
        return super.func_230254_b_(player, hand);
    }

    public void setGroup(Stream<SongvernEntity> stream) {
        stream.limit(this.getMaxSpawnedInChunk() - this.groupSize).filter(entity -> entity != this).forEach(entity -> entity.setGroupLeaader(this));
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
        return this.isGroupLeader() && this.groupSize < this.getMaxSpawnedInChunk();
    }

    public boolean isGroupLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.getDistanceSq(this.groupLeader) <= 121.0D;
    }

    public void moveToGroupLeader() {
        if (this.hasGroupLeader()) {
            this.getNavigator().tryMoveToEntityLiving(this.groupLeader, 1.0D);
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
            return 200 + taskOwnerIn.getRNG().nextInt(200) % 20;
        }

        public boolean shouldExecute() {
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
                List<SongvernEntity> list = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().grow(8.0D, 8.0D, 8.0D), predicate);
                SongvernEntity songvernEntity = list.stream().filter(SongvernEntity::canGroupGrow).findAny().orElse(this.taskOwner);
                songvernEntity.setGroup(list.stream().filter(entity -> !entity.hasGroupLeader()));
                return this.taskOwner.hasGroupLeader();
            }
        }

        public boolean shouldContinueExecuting() {
            return this.taskOwner.hasGroupLeader() && this.taskOwner.inRangeOfGroupLeader();
        }

        public void startExecuting() {
            this.navigateTimer = 0;
        }

        public void resetTask() {
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
