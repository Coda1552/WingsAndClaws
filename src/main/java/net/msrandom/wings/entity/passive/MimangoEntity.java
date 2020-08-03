package net.msrandom.wings.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.msrandom.wings.block.WingsBlocks;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.goal.MimangoHangGoal;
import net.msrandom.wings.entity.goal.MinmangoFlyGoal;

public class MimangoEntity extends TameableDragonEntity implements Flutterer {
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(MimangoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> HANGING = DataTracker.registerData(MimangoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final Ingredient TEMPT_ITEM = Ingredient.ofItems(WingsBlocks.MANGO_BUNCH.asItem());

    private MimangoHangGoal hangGoal;

    public MimangoEntity(EntityType<? extends MimangoEntity> type, World world) {
        super(type, world);
        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttributes().registerAttribute(EntityAttributes.GENERIC_FLYING_SPEED);
        this.getAttributeInstance(EntityAttributes.GENERIC_FLYING_SPEED).setBaseValue(0.8);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(14);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation flyingpathnavigator = new BirdNavigation(this, world);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(true);
        flyingpathnavigator.setCanEnterOpenDoors(true);
        return flyingpathnavigator;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.hangGoal = new MimangoHangGoal(this, 5.0D);
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false) {
            @Override
            public boolean canStart() {
                return super.canStart() && getState() == WanderState.FOLLOW;
            }
        });
        this.goalSelector.add(3, hangGoal);
        this.goalSelector.add(4, new MinmangoFlyGoal(this, 0.8D));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
        this.dataTracker.startTracking(HANGING, false);
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == WingsBlocks.MANGO_BUNCH.asItem();
    }

    @Override
    public EntityData initialize(WorldAccess world, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, CompoundTag dataTag) {
        setVariant(random.nextInt(5));
        return super.initialize(world, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!isTamed() && stack.getItem() == WingsBlocks.MANGO_BUNCH.asItem()) {
            if (random.nextInt(3) == 0) {
                this.setOwner(player);
                this.navigation.stop();
                this.goalSelector.remove(hangGoal);
                this.setTarget(null);
                this.setHealth(20.0F);
                this.world.sendEntityStatus(this, (byte) 7);
            } else {
                this.world.sendEntityStatus(this, (byte) 6);
            }
        }
        return super.interactMob(player, hand);
    }

    public boolean isHanging() {
        return this.dataTracker.get(HANGING);
    }

    public void setHanging(boolean isHanging) {
        this.dataTracker.set(HANGING, isHanging);
    }

    public void tick() {
        super.tick();
        if (this.isHanging()) {
            this.setVelocity(Vec3d.ZERO);
            this.setPos(this.getX(), (double) MathHelper.floor(this.getY()) + 1.0D - (double) this.getHeight(), this.getZ());
        }
    }

    public boolean isFlying() {
        return !this.onGround;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    public boolean isPushable() {
        return true;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }
}
