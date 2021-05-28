package coda.wingsandclaws.entity.item;

import coda.wingsandclaws.entity.passive.MimangoEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import coda.wingsandclaws.entity.WingsEntities;
import coda.wingsandclaws.item.WingsItems;

import java.util.Collections;
import java.util.List;

public class MimangoEggEntity extends LivingEntity {
    private final boolean startedInLeaves;
    private int hatchTime;

    public MimangoEggEntity(EntityType<? extends MimangoEggEntity> type, World world) {
        super(type, world);
        setSilent(true);
        startedInLeaves = false;
    }

    public MimangoEggEntity(World worldIn, BlockPos pos) {
        super(WingsEntities.MIMANGO_EGG.get(), worldIn);
        setSilent(true);
        setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        startedInLeaves = world.getBlockState(pos).getBlock().isIn(BlockTags.LEAVES);
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
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
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
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(WingsItems.MIMANGO_EGG.get())), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    public void tick() {
        if (!this.world.isRemote) {
            if (startedInLeaves && !world.getBlockState(getPosition()).getBlock().isIn(BlockTags.LEAVES)) {
                entityDropItem(new ItemStack(WingsItems.MIMANGO_EGG.get()));
                remove();
                return;
            }
            if (hatchTime++ >= 1200) {
                MimangoEntity mimangoEntity = WingsEntities.MIMANGO.get().create(this.world);
                if (mimangoEntity != null) {
                    mimangoEntity.setGrowingAge(-24000);
                    mimangoEntity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
                    mimangoEntity.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(mimangoEntity.getPosition()), SpawnReason.NATURAL, null, null);
                    world.getEntitiesWithinAABB(PlayerEntity.class, mimangoEntity.getBoundingBox().grow(15)).stream().reduce((p1, p2) -> mimangoEntity.getDistanceSq(p1) < mimangoEntity.getDistanceSq(p2) ? p1 : p2).ifPresent(mimangoEntity::setTamedBy);
                    this.world.addEntity(mimangoEntity);
                }

                this.world.setEntityState(this, (byte) 3);
                this.remove();
            }
        }
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
            ItemStack egg = new ItemStack(WingsItems.MIMANGO_EGG.get());
            if (!player.addItemStackToInventory(egg)) {
                player.dropItem(egg, false);
            }
            remove();
            return ActionResultType.SUCCESS;
        }
        return super.processInitialInteract(player, hand);
    }

    public static MimangoEggEntity getEgg(World world, BlockPos pos) {
        List<MimangoEggEntity> list = world.getEntitiesWithinAABB(MimangoEggEntity.class, new AxisAlignedBB(pos));
        return list.isEmpty() ? null : list.get(0);
    }
}
