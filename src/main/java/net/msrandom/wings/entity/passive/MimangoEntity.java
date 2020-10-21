package net.msrandom.wings.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.goal.MimangoHangGoal;
import net.msrandom.wings.entity.goal.MinmangoFlyGoal;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class MimangoEntity extends TameableDragonEntity implements IFlyingAnimal {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(MimangoEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HANGING = EntityDataManager.createKey(MimangoEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RARE = EntityDataManager.createKey(MimangoEntity.class, DataSerializers.BOOLEAN);
    private static final Ingredient TEMPT_ITEM = Ingredient.fromItems(WingsBlocks.MANGO_BUNCH.asItem());

    private MimangoHangGoal hangGoal;

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
        this.hangGoal = new MimangoHangGoal(this, 5.0D);
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false) {
            @Override
            public boolean shouldExecute() {
                return super.shouldExecute() && getState() == WanderState.FOLLOW;
            }
        });
        this.goalSelector.addGoal(3, hangGoal);
        this.goalSelector.addGoal(4, new MinmangoFlyGoal(this, 0.8D));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(RARE, false);
        this.dataManager.register(HANGING, false);
    }

    public int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }
    
    public boolean getRarity() {
        return this.dataManager.get(RARE);
    }

    public void setRarity(boolean rarity) {
        this.dataManager.set(RARE, rarity);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == WingsBlocks.MANGO_BUNCH.asItem();
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        setVariant(rand.nextInt(5));
        this.setRarity(rand.nextInt(100) == 1);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!isTamed() && stack.getItem() == WingsBlocks.MANGO_BUNCH.asItem()) {
            if (rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                this.setTamedBy(player);
                this.navigator.clearPath();
                this.goalSelector.removeGoal(hangGoal);
                this.setAttackTarget(null);
                this.setHealth(20.0F);
                this.playTameEffect(true);
                this.world.setEntityState(this, (byte) 7);
            } else {
                this.playTameEffect(false);
                this.world.setEntityState(this, (byte) 6);
            }
        }
        return super.processInteract(player, hand);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        MimangoEntity mimango = Objects.requireNonNull(WingsEntities.MIMANGO.create(this.world));
        UUID uuid = this.getOwnerId();
        if (uuid != null) {
            mimango.setOwnerId(uuid);
            mimango.setTamed(true);
        }
        return mimango;
    }

    public boolean isHanging() {
        return this.dataManager.get(HANGING);
    }

    public void setHanging(boolean isHanging) {
        this.dataManager.set(HANGING, isHanging);
    }

    public void tick() {
        super.tick();
        if (this.isHanging()) {
            this.setMotion(Vec3d.ZERO);
            this.setRawPosition(this.getPosX(), (double) MathHelper.floor(this.getPosY()) + 1.0D - (double)this.getHeight(), this.getPosZ());
        }
    }

    public boolean isFlying() {
        return !this.onGround;
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
