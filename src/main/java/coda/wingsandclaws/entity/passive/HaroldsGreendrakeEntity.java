package coda.wingsandclaws.entity.passive;

import coda.wingsandclaws.client.WingsSounds;
import coda.wingsandclaws.entity.WingsEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.Random;

public class HaroldsGreendrakeEntity extends AnimalEntity {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(HaroldsGreendrakeEntity.class, DataSerializers.ITEMSTACK);

    public HaroldsGreendrakeEntity(EntityType<? extends HaroldsGreendrakeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.fromItems(Items.POTATO), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(ITEM, ItemStack.EMPTY);
    }

    public static boolean canHaroldsSpawn(EntityType<? extends HaroldsGreendrakeEntity> animal, IWorld worldIn, SpawnReason reason, BlockPos pos, Random random) {
        return worldIn.getBlockState(pos.down()).getBlock() == Blocks.GRASS_BLOCK && worldIn.getLightSubtracted(pos, 0) > 4;
    }

    public static AttributeModifierMap.MutableAttribute registerGreendrakeAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.1883).createMutableAttribute(Attributes.MAX_HEALTH, 12);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.POTATO;
    }

    protected SoundEvent getAmbientSound() {
        return WingsSounds.HAROLD_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return WingsSounds.HAROLD_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return WingsSounds.HAROLD_DEATH.get();
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public boolean isPotionApplicable(EffectInstance effect) {
        if (effect.getPotion() == Effects.POISON) {
            PotionEvent.PotionApplicableEvent event = new PotionEvent.PotionApplicableEvent(this, effect);
            MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == Event.Result.ALLOW;
        }
        return super.isPotionApplicable(effect);
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slot, ItemStack stack) {
        if (slot == EquipmentSlotType.MAINHAND) {
            dataManager.set(ITEM, stack);
        } else {
            super.setItemStackToSlot(slot, stack);
        }
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slot) {
        return slot == EquipmentSlotType.MAINHAND ? dataManager.get(ITEM) : super.getItemStackFromSlot(slot);
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        float maxHealth = this.getMaxHealth();
        float health = this.getHealth();
        if (stack.getItem() == Items.POISONOUS_POTATO) {
            if (health < maxHealth) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                heal(4);
                double x = this.rand.nextGaussian() * 0.02D;
                double y = this.rand.nextGaussian() * 0.02D;
                double z = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), x, y, z);
                return ActionResultType.SUCCESS;
            }
        } else if (stack.getItem() == Items.APPLE) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            final ItemStack apple = stack.copy();
            apple.setCount(1);
            setHeldItem(Hand.MAIN_HAND, apple);
        }
        return super.func_230254_b_(player, hand);
    }

    public HaroldsGreendrakeEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return WingsEntities.HAROLDS_GREENDRAKE.get().create(serverWorld);
    }

    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return this.isChild() ? size.height * 0.95F : 0.7F;
    }
}
