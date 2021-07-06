package coda.wingsandclaws.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

public class SaddledThunderTailEggEntity extends LivingEntity {
    private int hatchTime;

    public SaddledThunderTailEggEntity(EntityType<? extends SaddledThunderTailEggEntity> type, World worldIn) {
        super(type, worldIn);
        setSilent(true);
    }

    public SaddledThunderTailEggEntity(World worldIn, double x, double y, double z) {
        this(null/*WingsEntities.SADDLED_THUNDER_TAIL_EGG*/, worldIn);
        setPos(x, y, z);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {}

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.hatchTime = compound.getInt("HatchTime");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("HatchTime", hatchTime);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                //this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(WingsItems.SADDLED_THUNDER_TAIL_EGG)), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    public void tick() {
        this.setAirSupply(300);
        if (!this.level.isClientSide) {
            if (hatchTime++ >= 24000) {
/*                SaddledThunderTailEntity thunderTailEntity = WingsEntities.SADDLED_THUNDER_TAIL.create(this.world);
                if (thunderTailEntity != null) {
                    thunderTailEntity.setGrowingAge(-24000);
                    thunderTailEntity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
                    thunderTailEntity.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(thunderTailEntity.getPosition()), SpawnReason.NATURAL, null, null);
                    world.getEntitiesWithinAABB(PlayerEntity.class, thunderTailEntity.getBoundingBox().grow(15)).stream().reduce((p1, p2) -> thunderTailEntity.getDistanceSq(p1) < thunderTailEntity.getDistanceSq(p2) ? p1 : p2).ifPresent(thunderTailEntity::setTamedBy);
                    this.world.addEntity(thunderTailEntity);
                }*/

                this.level.broadcastEntityEvent(this, (byte) 3);
                this.remove();
            }
        }
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            ItemStack egg = ItemStack.EMPTY;//new ItemStack(WingsItems.SADDLED_THUNDER_TAIL_EGG);
            if (!player.addItem(egg)) {
                player.drop(egg, false);
            }
            remove();
            return ActionResultType.SUCCESS;
        }

        return super.interact(player, hand);
    }
}
