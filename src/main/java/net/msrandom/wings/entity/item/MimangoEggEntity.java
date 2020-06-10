package net.msrandom.wings.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;

import java.util.Collections;

public class MimangoEggEntity extends LivingEntity {
    public MimangoEggEntity(EntityType<? extends MimangoEggEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        setSilent(true);
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

    protected void updateAir(int air) {
        if (this.isAlive() && !this.isInWaterOrBubbleColumn()) {
            this.setAir(air - 1);
            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAir(300);
        }

    }

    @Override
    public void baseTick() {
        int i = this.getAir();
        super.baseTick();
        this.updateAir(i);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
