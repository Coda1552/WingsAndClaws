package net.msrandom.wings.entity.passive;

import com.google.gson.stream.JsonReader;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
import net.minecraft.resources.IResource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.TameableDragonEntity;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class HatchetBeakEntity extends TameableDragonEntity implements IFlyingAnimal {
    private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(HatchetBeakEntity.class, DataSerializers.BOOLEAN);
    private static Object2IntMap<Item> points;
    private final Map<UUID, AtomicInteger> players = new HashMap<>();
    private Vector3d target;
    private int ticksAfloat;
    private int flyTime;

    public HatchetBeakEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
        moveController = new FlyingMovementController(this, 30, false);
        setNoGravity(true);
        if (!worldIn.isRemote && points == null) {
            try {
                points = new Object2IntOpenHashMap<>();
                MinecraftServer server = Objects.requireNonNull(worldIn.getServer());
                IResource resource = server.getDataPackRegistries().getResourceManager().getResource(new ResourceLocation(WingsAndClaws.MOD_ID, "tame_items/hatchet_beak.json"));
                InputStream stream = resource.getInputStream();
                JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    int value = reader.nextInt();
                    if (name.startsWith("#")) {
                        ITag<Item> items = ItemTags.getCollection().get(new ResourceLocation(name.replace("#", "")));
                        for (Item element : items.getAllElements()) {
                            points.put(element, value);
                        }
                    } else {
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
                        if (item != null && item != Items.AIR) {
                            points.put(item, value);
                        }
                    }
                }
                reader.endObject();
                reader.close();
            } catch (IOException e) {
                WingsAndClaws.LOGGER.error("Failed to read Hatchet Beak item tame points", e);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1, 40));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 15, 1));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SADDLED, false);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return new FlyingPathNavigator(this, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute registerHBAttributes() {
        return LivingEntity.registerAttributes().createMutableAttribute(Attributes.FOLLOW_RANGE, 16).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3).createMutableAttribute(Attributes.MAX_HEALTH, 60).createMutableAttribute(Attributes.FLYING_SPEED, 10).createMutableAttribute(Attributes.ATTACK_DAMAGE, 4);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tamed ? 90 : 60);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8);
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean isFlying() {
        return world.isAirBlock(new BlockPos(getPosX(), getPosY() - 0.01, getPosZ()));
    }

    public boolean hasSaddle() {
        return dataManager.get(SADDLED);
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
                float rotationDifference = MathHelper.wrapDegrees(passenger.rotationYaw) - MathHelper.wrapDegrees(rotationYaw);
                float sign = Math.signum(rotationDifference);
                if (MathHelper.abs(rotationDifference) > 3) rotationYaw += sign * 3f;

                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch = passenger.rotationPitch * 0.5F;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.renderYawOffset;
                float f = passenger.moveStrafing * 0.25F;

                float f1;
                if (isFlying()) {
                    f1 = Math.max(0.6f, moveForward + passenger.moveForward * 5 + 3);
                } else {
                    f1 = passenger.moveForward * 0.25F;
                    if (f1 <= 0.0F) {
                        f1 *= 0.25F;
                    }
                }

                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
                if (this.canPassengerSteer()) {
                    this.setAIMoveSpeed((float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                    float verticalMotion = 0f;
                    if (isFlying()) {
                        verticalMotion = (rotationPitch / -22.5f) * 2;
                        if (verticalMotion <= 0) {
                            setMotion(getMotion().add(0, verticalMotion / 22.5f, 0));
                        }
                    } else if (world.isRemote) {
                        checkFlight();
                    }
                    super.travel(new Vector3d(f, positionIn.y + verticalMotion, f1));
                } else if (passenger instanceof PlayerEntity) {
                    this.setMotion(Vector3d.ZERO);
                }

                this.prevLimbSwingAmount = this.limbSwingAmount;
                double d2 = this.getPosX() - this.prevPosX;
                double d3 = this.getPosX() - this.prevPosX;
                double d4 = this.getPosZ() - this.prevPosZ;
                float f4 = MathHelper.sqrt(d2 * d2 + d3 * d3 + d4 * d4) * 4.0F;
                if (f4 > 1.0F) {
                    f4 = 1.0F;
                }

                this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
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
    private void checkFlight() {
        if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
            setMotion(getMotion().add(0, 0.2, 0));
        }
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
            if (points.containsKey(stack.getItem())) {
                AtomicInteger playerPoints = players.computeIfAbsent(player.getUniqueID(), k -> new AtomicInteger());
                playerPoints.set(playerPoints.get() + points.getInt(stack.getItem()));
                if (!player.abilities.isCreativeMode) stack.shrink(1);
                if (playerPoints.get() >= 100 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.setTamedBy(player);
                    this.navigator.clearPath();
                    this.setAttackTarget(null);
                    this.setHealth(20.0F);
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
    private Vector3d getTargetPosition(boolean land) {
        if (this.isInWaterOrBubbleColumn()) {
            Vector3d vec3d = RandomPositionGenerator.getLandPos(this, 15, 7);
            ticksAfloat = 0;
            return vec3d == null ? RandomPositionGenerator.findRandomTarget(this, 10, 7) : vec3d;
        }

        if (!shouldSleep() && !land) {
            flyTime = rand.nextInt(1800) + 600;
            return getAirPosition();
        }

        ticksAfloat = 0;
        return RandomPositionGenerator.getLandPos(this, 10, 7);
    }

    private Vector3d getAirPosition() {
        BlockPos pos = getPosition().up(getPosY() > world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) getPosX(), (int) getPosZ()) + 20 ? 0 : rand.nextInt(12) + 12);
        int original = pos.getY();
        while (pos.getY() <= original - 8 && !world.isAirBlock(pos)) {
            pos = pos.down();
        }

        if (pos.getY() == original - 8) {
            pos = pos.up(8);

            while (pos.getY() <= original + 8 && !world.isAirBlock(pos)) {
                pos = pos.up();
            }

            if (pos.getY() == original + 8) return null;
        }

        int xDistance = rand.nextInt(32) + 64;
        int zDistance = rand.nextInt(32) + 64;
        double rotation = Math.toRadians(rand.nextInt(361));
        return new Vector3d(pos.getX() + Math.sin(rotation) * xDistance, pos.getY(), pos.getZ() + Math.cos(-rotation) * zDistance);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            boolean flying = isFlying();
            boolean grounded = !flying || ticksExisted <= 25;

            if (!grounded && ticksAfloat >= flyTime) {
                target = null;
            }

            if (target == null && (!flying || rand.nextInt(10) == 0)) {
                if (!grounded) ++ticksAfloat;
                target = getTargetPosition((grounded && rand.nextFloat() >= 0.05f) || ticksAfloat >= 300 && rand.nextFloat() <= 0.7f);
            }

            if (target != null) {
                moveController.setMoveTo(target.getX(), target.getY(), target.getZ(), getMotion().getY() <= 0 || !flying ? 0.6 : 0.2);
                if (getDistanceSq(target) <= 9) {
                    target = null;
                }
            }
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof LivingEntity;
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
}
