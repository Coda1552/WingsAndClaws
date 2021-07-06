package coda.wingsandclaws.entity;

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
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import coda.wingsandclaws.init.WingsSounds;
import coda.wingsandclaws.init.WingsBlocks;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.entity.goal.FlyGoal;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class MimangoEntity extends TameableDragonEntity implements IFlyingAnimal {
    private static final EntityPredicate CAN_BREED = new EntityPredicate().range(64.0D).allowInvulnerable().allowSameTeam().allowUnseeable();
    private static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(MimangoEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> HIDING = EntityDataManager.defineId(MimangoEntity.class, DataSerializers.BOOLEAN);
    //private static final Ingredient TEMPT_ITEM = Ingredient.fromItems(WingsBlocks.MANGO_BUNCH.asItem());

    public MimangoEntity(EntityType<? extends MimangoEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new FlyingMovementController(this, 10, true);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
    }

    public static AttributeModifierMap.MutableAttribute registerMimangoAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.FLYING_SPEED, 0.8).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.MAX_HEALTH, 8);
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
                List<AnimalEntity> list = this.level.getNearbyEntities(MimangoEntity.class, CAN_BREED, this.animal, this.animal.getBoundingBox().inflate(64.0D));
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
        this.goalSelector.addGoal(3, new HangGoal());
        this.goalSelector.addGoal(4, new FlyGoal<>(this, entity -> !entity.isHiding()));
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, OcelotEntity.class, 6, 1, 1.2));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(HIDING, false);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.COCOA_BEANS;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        setVariant(random.nextInt(5));
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.setHiding(false);
        if (source == DamageSource.IN_WALL && !level.getBlockState(blockPosition()).canOcclude()) {
            setDeltaMovement(getDeltaMovement().add(0, -0.05, 0));
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            playSound(WingsSounds.MIMANGO_HAPPY.get(), getSoundVolume(), getVoicePitch());
            return ActionResultType.SUCCESS;
        }

        if (handleSpawnEgg(player, stack)) return ActionResultType.SUCCESS;

        if (!isTame() && stack.getItem() == WingsBlocks.MANGO_BUNCH.get().asItem()) {
            if (random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setHealth(20.0F);
                this.level.broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level.broadcastEntityEvent(this, (byte) 6);
            }
            return ActionResultType.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", getVariant());
        compound.putBoolean("Hiding", isHiding());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        setVariant(compound.getInt("Variant"));
        setHiding(compound.getBoolean("Hiding"));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return WingsSounds.MIMANGO_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return WingsSounds.MIMANGO_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return WingsSounds.MIMANGO_DEATH.get();
    }

    public boolean isHiding() {
        return this.entityData.get(HIDING);
    }

    public void setHiding(boolean isHanging) {
        this.entityData.set(HIDING, isHanging);
    }

    public void tick() {
        super.tick();
        if (isOrderedToSit()) setDeltaMovement(getDeltaMovement().add(0, -0.05, 0));
        if (this.isHiding()) {
            this.setDeltaMovement(Vector3d.ZERO);
            this.setPosRaw(this.getX(), (double) MathHelper.floor(this.getY()) + 1.0D - (double) this.getBbHeight(), this.getZ());
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

    class HangGoal extends MoveToBlockGoal
    {
        public HangGoal() {
            super(MimangoEntity.this, 5, 16, 16);
            setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
        }

        @Override
        protected boolean isValidTarget(IWorldReader worldIn, BlockPos pos) {
            return level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).is(BlockTags.LEAVES);
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        @Override
        public void tick() {
            super.tick();
            if (isReachedTarget())
            {
                setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                setHiding(true);
            }
            else setHiding(false);
        }

        @Override
        public void stop() {
            super.stop();
            setHiding(false);
        }
    }
}
