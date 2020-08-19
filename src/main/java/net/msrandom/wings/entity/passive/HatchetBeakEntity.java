package net.msrandom.wings.entity.passive;

import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonReader;
import com.sun.istack.internal.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.entity.TameableDragonEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class HatchetBeakEntity extends TameableDragonEntity implements Flutterer {
    private static final TrackedData<Boolean> SADDLE = DataTracker.registerData(HatchetBeakEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static Map<Item, Integer> points;
    private final Map<UUID, AtomicInteger> players = new HashMap<>();

    public HatchetBeakEntity(EntityType<? extends TameableDragonEntity> type, World world) {
        super(type, world);
        moveControl = new FlightMoveControl(this, 30, false);
        setNoGravity(true);
        if (!world.isClient && points == null) {
            ImmutableMap.Builder<Item, Integer> builder = ImmutableMap.builder();
            MinecraftServer server = Objects.requireNonNull(world.getServer());
            try {
                //Probably crashes
                Resource resource = ((ResourceManager) server.getDataPackManager()).getResource(new Identifier(WingsAndClaws.MOD_ID, "tame_items/hatchet_beak.json"));
                InputStream stream = resource.getInputStream();
                JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    int points = reader.nextInt();
                    if (name.startsWith("#")) {
                        Tag<Item> items = ItemTags.getContainer().getOrCreate(new Identifier(name.replace("#", "")));
                        for (Item element : items.values()) {
                            builder.put(element, points);
                        }
                    } else {
                        Item item = Registry.ITEM.get(new Identifier(name));
                        if (item != Items.AIR) {
                            builder.put(item, points);
                        }
                    }
                }
                reader.endObject();
                reader.close();
            } catch (IOException e) {
                WingsAndClaws.LOGGER.error("Failed to read Hatchet Peak item tame points", e);
            }

            points = builder.build();
        }
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        //this.goalSelector.add(1, new RandomSwimmingGoal(this, 1, 40));
        //this.goalSelector.add(6, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 15, 1));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D) {
            @Nullable
            @Override
            protected Vec3d getWanderTarget() {
                return TargetFinder.findAirTarget(HatchetBeakEntity.this, 32, 32, getRotationVector(), ((float) Math.PI / 2F), 2, 1);
            }
        });
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SADDLE, false);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
    }

    public static DefaultAttributeContainer.Builder registerHBAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3).add(EntityAttributes.GENERIC_MAX_HEALTH, 60).add(EntityAttributes.GENERIC_FLYING_SPEED, 10).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(tamed ? 90 : 60);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(8);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    public boolean isFlying() {
        return !onGround;
    }

    @Override
    public void mobTick() {
        super.mobTick();

        Vec3d vec3d = this.getVelocity();
        if (!this.onGround && vec3d.y < 0.0D) {
            this.setVelocity(vec3d.multiply(1.0D, 0.6D, 1.0D));
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.FERMENTED_SPIDER_EYE;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!isBaby() && isTamed() && isOwner(player)) {
            if (stack.getItem() == Items.SADDLE && !this.dataTracker.get(SADDLE))
                this.dataTracker.set(SADDLE, true);
            else if (stack.getItem() == Items.SHEARS && this.dataTracker.get(SADDLE))
                this.dataTracker.set(SADDLE, false);
            else if (stack.isEmpty()) player.startRiding(this);
        } else {
            if (points.containsKey(stack.getItem())) {
                AtomicInteger playerPoints = players.computeIfAbsent(player.getUuid(), k -> new AtomicInteger());
                playerPoints.set(playerPoints.get() + points.get(stack.getItem()));
                if (!player.abilities.creativeMode) stack.decrement(1);
                if (playerPoints.get() >= 100) {
                    this.setOwner(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setHealth(20.0F);
                    players.clear();
                    this.world.sendEntityStatus(this, (byte) 7);
                } else {
                    this.world.sendEntityStatus(this, (byte) 6);
                }
            }
        }
        return super.interactMob(player, hand);
    }

    @Override
    public ItemStack getEgg() {
        return /*new ItemStack(WingsItems.HATCHET_BEAK_EGG)*/ ItemStack.EMPTY;
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compound) {
        super.writeCustomDataToTag(compound);
        compound.putBoolean("Saddle", this.dataTracker.get(SADDLE));
        if (!players.isEmpty()) {
            ListTag list = new ListTag();
            for (Map.Entry<UUID, AtomicInteger> entry : players.entrySet()) {
                CompoundTag nbt = new CompoundTag();
                nbt.putUuid("UUID", entry.getKey());
                nbt.putInt("Value", entry.getValue().get());
                list.add(nbt);
            }
            compound.put("Players", list);
        }
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound) {
        super.readCustomDataFromTag(compound);
        this.dataTracker.set(SADDLE, compound.getBoolean("Saddle"));
        if (compound.contains("Players")) {
            ListTag list = compound.getList("Players", 10);
            for (net.minecraft.nbt.Tag entry : list) {
                CompoundTag nbt = (CompoundTag) entry;
                players.put(nbt.getUuid("UUID"), new AtomicInteger(nbt.getInt("Values")));
            }
        }
    }
}
