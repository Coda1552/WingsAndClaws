package net.msrandom.wings.entity.passive;

import com.google.gson.stream.JsonReader;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.RandomPositionGenerator;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.item.WingsItems;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HatchetBeakEntity extends TameableDragonEntity implements IFlyingAnimal {
    private static final DataParameter<Boolean> SADDLE = EntityDataManager.createKey(HatchetBeakEntity.class, DataSerializers.BOOLEAN);
    private static Object2IntMap<Item> points;
    private final Map<UUID, AtomicInteger> players = new HashMap<>();
    private Vec3d target;

    public HatchetBeakEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
        moveController = new FlyingMovementController(this, 30, false);
        setNoGravity(true);
        if (!worldIn.isRemote && points == null) {
            try {
                points = new Object2IntOpenHashMap<>();
                MinecraftServer server = Objects.requireNonNull(worldIn.getServer());
                IResource resource = server.getResourceManager().getResource(new ResourceLocation(WingsAndClaws.MOD_ID, "tame_items/hatchet_beak.json"));
                InputStream stream = resource.getInputStream();
                JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    int value = reader.nextInt();
                    if (name.startsWith("#")) {
                        Tag<Item> items = ItemTags.getCollection().getOrCreate(new ResourceLocation(name.replace("#", "")));
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
            } catch (IOException e) {
                WingsAndClaws.LOGGER.error("Failed to read Hatchet Peak item tame points", e);
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
        this.dataManager.register(SADDLE, false);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return new FlyingPathNavigator(this, worldIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(10);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(tamed ? 90 : 60);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean isFlying() {
        return !onGround;
    }

    @Override
    public boolean isSleeping() {
        return !isFlying() && shouldSleep();
    }

    @Override
    public void livingTick() {
        super.livingTick();

        Vec3d vec3d = this.getMotion();
        if (!this.onGround && vec3d.y < 0.0D) {
            this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.FERMENTED_SPIDER_EYE;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!isChild() && isTamed() && isOwner(player)) {
            if (stack.getItem() == Items.SADDLE && !this.dataManager.get(SADDLE))
                this.dataManager.set(SADDLE, true);
            else if (stack.getItem() == Items.SHEARS && this.dataManager.get(SADDLE))
                this.dataManager.set(SADDLE, false);
            else if (stack.isEmpty()) player.startRiding(this);
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
            }
        }
        return super.processInteract(player, hand);
    }

    @Nullable
    private Vec3d getTargetPosition(boolean land) {
        if (this.isInWaterOrBubbleColumn()) {
            Vec3d vec3d = RandomPositionGenerator.getLandPos(this, 15, 7);
            return vec3d == null ? RandomPositionGenerator.findRandomTarget(this, 10, 7) : vec3d;
        }

        if (!shouldSleep() && !land) {
            return getAirPosition();
        }

        return RandomPositionGenerator.getLandPos(this, 10, 7);
    }

    private Vec3d getAirPosition() {
        BlockPos pos = getPosition().up(rand.nextInt(12) + 32);
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
        return new Vec3d(pos.getX() + Math.sin(rotation) * xDistance, pos.getY(), pos.getZ() + Math.cos(-rotation) * zDistance);
    }

    @Override
    public void tick() {
        super.tick();
        if (target == null) {
            target = getTargetPosition(isFlying() && rand.nextFloat() <= 0.1f);
        }

        if (target != null) {
            moveController.setMoveTo(target.getX(), target.getY(), target.getZ(), 2);
            if (getDistanceSq(target) <= 9) {
                target = null;
            }
        }
    }

    @Override
    public ItemStack getEgg() {
        return new ItemStack(WingsItems.HATCHET_BEAK_EGG);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Saddle", this.dataManager.get(SADDLE));
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
        this.dataManager.set(SADDLE, compound.getBoolean("Saddle"));
        if (compound.contains("Players")) {
            ListNBT list = compound.getList("Players", 10);
            for (INBT entry : list) {
                CompoundNBT nbt = (CompoundNBT) entry;
                players.put(nbt.getUniqueId("UUID"), new AtomicInteger(nbt.getInt("Values")));
            }
        }
    }
}
