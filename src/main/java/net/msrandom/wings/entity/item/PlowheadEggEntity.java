package net.msrandom.wings.entity.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;
import net.msrandom.wings.item.WingsItems;

import java.util.Collections;

public class PlowheadEggEntity extends LivingEntity {
	private int hatchTime;

	public PlowheadEggEntity(EntityType<? extends PlowheadEggEntity> type, World world) {
		super(type, world);
		setSilent(true);
	}

	public PlowheadEggEntity(World world, LivingEntity placer) {
		this(WingsEntities.PLOWHEAD_EGG, world);
		setPos(placer.getX(), placer.getY(), placer.getZ());
	}

	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return false;
	}

	@Override
	public void readCustomDataFromTag(CompoundTag compound) {
		super.readCustomDataFromTag(compound);
		this.hatchTime = compound.getInt("HatchTime");
	}

	@Override
	public void writeCustomDataToTag(CompoundTag compound) {
		super.writeCustomDataToTag(compound);
		compound.putInt("HatchTime", hatchTime);
	}

	@Environment(EnvType.CLIENT)
	public void handleStatus(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(WingsItems.ICY_PLOWHEAD_EGG)), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
	}

	@Override
	public void tick() {
		super.tick();
		this.setAir(300);
		if (!this.world.isClient) {
			if (isInWaterOrBubbleColumn() && hatchTime++ >= 4800) {
				IcyPlowheadEntity plowhead = WingsEntities.ICY_PLOWHEAD.create(this.world);
				if (plowhead != null) {
					plowhead.setBreedingAge(-24000);
					plowhead.setLocationAndAngles(this.getX(), this.getY(), this.getZ(), this.rotationYaw, 0.0F);
					plowhead.initialize(world, world.getLocalDifficulty(plowhead.getBlockPos()), SpawnReason.BREEDING, null, null);
					this.world.spawnEntity(plowhead);
				}

				this.world.sendEntityStatus(this, (byte) 3);
				this.remove();
			}
		}
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isEmpty()) {
			if (player.giveItemStack(new ItemStack(WingsItems.ICY_PLOWHEAD_EGG))) {
				remove();
			}
			return ActionResult.SUCCESS;
		}

		return super.interact(player, hand);
	}
}
