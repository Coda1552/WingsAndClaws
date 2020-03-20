package random.wings.entity.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import random.wings.entity.TameableDragonEntity;

public class HatchetBeakEntity extends TameableDragonEntity {
    private static final DataParameter<Boolean> SADDLE = EntityDataManager.createKey(HatchetBeakEntity.class, DataSerializers.BOOLEAN);

    public HatchetBeakEntity(EntityType<? extends TameableDragonEntity> type, World worldIn) {
        super(type, worldIn);
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
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(isTamed() ? 90 : 60);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(isTamed() ? 90 : 60);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.FERMENTED_SPIDER_EYE;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        if (!isChild() && isTamed() && isOwner(player)) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() == Items.SADDLE && !this.dataManager.get(SADDLE)) this.dataManager.set(SADDLE, true);
            else if (stack.getItem() == Items.SHEARS && this.dataManager.get(SADDLE))
                this.dataManager.set(SADDLE, false);
            else if (stack.isEmpty()) player.startRiding(this);
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Saddle", this.dataManager.get(SADDLE));
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(SADDLE, compound.getBoolean("Saddle"));
    }
}
