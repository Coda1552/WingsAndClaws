package net.msrandom.wings.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.SaddledThunderTailEntity;
import net.msrandom.wings.item.WingsItems;

import java.util.Collections;

public class SaddledThunderTailEggEntity extends LivingEntity {
    private int hatchTime;

    public SaddledThunderTailEggEntity(EntityType<? extends SaddledThunderTailEggEntity> type, World worldIn) {
        super(type, worldIn);
        setSilent(true);
    }

    public SaddledThunderTailEggEntity(World worldIn, double x, double y, double z) {
        this(null/*WingsEntities.SADDLED_THUNDER_TAIL_EGG*/, worldIn);
        setPosition(x, y, z);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {}

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.hatchTime = compound.getInt("HatchTime");
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("HatchTime", hatchTime);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                //this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(WingsItems.SADDLED_THUNDER_TAIL_EGG)), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    public void tick() {
        this.setAir(300);
        if (!this.world.isRemote) {
            if (hatchTime++ >= 24000) {
/*                SaddledThunderTailEntity thunderTailEntity = WingsEntities.SADDLED_THUNDER_TAIL.create(this.world);
                if (thunderTailEntity != null) {
                    thunderTailEntity.setGrowingAge(-24000);
                    thunderTailEntity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
                    thunderTailEntity.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(thunderTailEntity.getPosition()), SpawnReason.NATURAL, null, null);
                    world.getEntitiesWithinAABB(PlayerEntity.class, thunderTailEntity.getBoundingBox().grow(15)).stream().reduce((p1, p2) -> thunderTailEntity.getDistanceSq(p1) < thunderTailEntity.getDistanceSq(p2) ? p1 : p2).ifPresent(thunderTailEntity::setTamedBy);
                    this.world.addEntity(thunderTailEntity);
                }*/

                this.world.setEntityState(this, (byte) 3);
                this.remove();
            }
        }
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
            ItemStack egg = ItemStack.EMPTY;//new ItemStack(WingsItems.SADDLED_THUNDER_TAIL_EGG);
            if (!player.addItemStackToInventory(egg)) {
                player.dropItem(egg, false);
            }
            remove();
            return ActionResultType.SUCCESS;
        }

        return super.processInitialInteract(player, hand);
    }
}
