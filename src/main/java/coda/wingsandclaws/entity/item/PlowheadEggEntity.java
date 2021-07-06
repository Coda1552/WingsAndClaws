package coda.wingsandclaws.entity.item;

import coda.wingsandclaws.entity.IcyPlowheadEntity;
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
import coda.wingsandclaws.init.WingsEntities;
import coda.wingsandclaws.init.WingsItems;

import java.util.Collections;

public class PlowheadEggEntity extends LivingEntity {
	private int hatchTime;

	public PlowheadEggEntity(EntityType<? extends PlowheadEggEntity> type, World world) {
		super(type, world);
		setSilent(true);
	}

	public PlowheadEggEntity(World worldIn, double x, double y, double z) {
		this(WingsEntities.ICY_PLOWHEAD_EGG.get(), worldIn);
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
				this.level.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(WingsItems.ICY_PLOWHEAD_EGG.get())), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		this.setAirSupply(300);
		if (!this.level.isClientSide) {
			if (isInWaterOrBubble() && hatchTime++ >= 4800) {
				IcyPlowheadEntity plowhead = WingsEntities.ICY_PLOWHEAD.get().create(this.level);
				if (plowhead != null) {
					plowhead.setAge(-24000);
					plowhead.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
					plowhead.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(plowhead.blockPosition()), SpawnReason.BREEDING, null, null);
					this.level.addFreshEntity(plowhead);
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
            ItemStack egg = new ItemStack(WingsItems.ICY_PLOWHEAD_EGG.get());
            if (!player.addItem(egg)) {
                player.drop(egg, false);
            }
            remove();
			return ActionResultType.SUCCESS;
		}

		return super.interact(player, hand);
	}
}
