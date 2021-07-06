package coda.wingsandclaws.entity.item;

import coda.wingsandclaws.entity.MimangoEntity;
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
import coda.wingsandclaws.init.WingsEntities;
import coda.wingsandclaws.init.WingsItems;

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
        setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        startedInLeaves = level.getBlockState(pos).getBlock().is(BlockTags.LEAVES);
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
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.setAirSupply(300);
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
                this.level.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(WingsItems.MIMANGO_EGG.get())), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            if (startedInLeaves && !level.getBlockState(blockPosition()).getBlock().is(BlockTags.LEAVES)) {
                spawnAtLocation(new ItemStack(WingsItems.MIMANGO_EGG.get()));
                remove();
                return;
            }
            if (hatchTime++ >= 1200) {
                MimangoEntity mimangoEntity = WingsEntities.MIMANGO.get().create(this.level);
                if (mimangoEntity != null) {
                    mimangoEntity.setAge(-24000);
                    mimangoEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
                    mimangoEntity.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(mimangoEntity.blockPosition()), SpawnReason.NATURAL, null, null);
                    level.getEntitiesOfClass(PlayerEntity.class, mimangoEntity.getBoundingBox().inflate(15)).stream().reduce((p1, p2) -> mimangoEntity.distanceToSqr(p1) < mimangoEntity.distanceToSqr(p2) ? p1 : p2).ifPresent(mimangoEntity::tame);
                    this.level.addFreshEntity(mimangoEntity);
                }

                this.level.broadcastEntityEvent(this, (byte) 3);
                this.remove();
            }
        }
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            ItemStack egg = new ItemStack(WingsItems.MIMANGO_EGG.get());
            if (!player.addItem(egg)) {
                player.drop(egg, false);
            }
            remove();
            return ActionResultType.SUCCESS;
        }
        return super.interact(player, hand);
    }

    public static MimangoEggEntity getEgg(World world, BlockPos pos) {
        List<MimangoEggEntity> list = world.getEntitiesOfClass(MimangoEggEntity.class, new AxisAlignedBB(pos));
        return list.isEmpty() ? null : list.get(0);
    }
}
