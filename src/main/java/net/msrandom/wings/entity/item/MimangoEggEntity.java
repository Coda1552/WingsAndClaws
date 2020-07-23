package net.msrandom.wings.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.passive.MimangoEntity;
import net.msrandom.wings.item.WingsItems;

import java.util.Collections;

public class MimangoEggEntity extends LivingEntity {
    private int hatchTime;

    public MimangoEggEntity(EntityType<? extends MimangoEggEntity> type, World world) {
        super(type, world);
        setSilent(true);
    }

    public MimangoEggEntity(World worldIn, LivingEntity placer) {
        this(WingsEntities.MIMANGO_EGG, worldIn);
        setPosition(placer.getPosX(), placer.getPosY(), placer.getPosZ());
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
    public void baseTick() {
        super.baseTick();
        this.setAir(300);
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
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(WingsItems.MIMANGO_EGG)), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isRemote) {
            if (hatchTime++ >= 200) {
                MimangoEntity mimangoEntity = WingsEntities.MIMANGO.create(this.world);
                if (mimangoEntity != null) {
                    mimangoEntity.setGrowingAge(-24000);
                    mimangoEntity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
                    this.world.addEntity(mimangoEntity);
                }

                this.world.setEntityState(this, (byte) 3);
                this.remove();
            }
        }
    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
            if (player.addItemStackToInventory(new ItemStack(WingsItems.MIMANGO_EGG))) {
                remove();
            }
            return true;
        }
        return super.processInitialInteract(player, hand);
    }
}
