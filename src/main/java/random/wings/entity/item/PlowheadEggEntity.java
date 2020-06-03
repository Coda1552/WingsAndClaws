package random.wings.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import random.wings.entity.WingsEntities;
import random.wings.entity.monster.IcyPlowheadEntity;
import random.wings.item.WingsItems;

public class PlowheadEggEntity extends Entity {
	private int hatchTime;

	public PlowheadEggEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	public PlowheadEggEntity(World worldIn, LivingEntity placer) {
		super(WingsEntities.PLOWHEAD_EGG, worldIn);
		setPosition(placer.getPosX(), placer.getPosY(), placer.getPosZ());
	}

	@Override
	protected void registerData() {
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.hatchTime = compound.getInt("HatchTime");
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("HatchTime", hatchTime);
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(WingsItems.ICY_PLOWHEAD_EGG)), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.world.isRemote) {
			setMotion(getMotion().add(0, -0.005, 0));
			if (isInWaterOrBubbleColumn() && hatchTime++ >= 4800) {
				if (this.rand.nextInt(8) == 0) {
					int i = 1;
					if (this.rand.nextInt(32) == 0) {
						i = 4;
					}

					for (int j = 0; j < i; ++j) {
						IcyPlowheadEntity plowhead = WingsEntities.ICY_PLOWHEAD.create(this.world);
						if (plowhead != null) {
							plowhead.setGrowingAge(-24000);
							plowhead.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
							this.world.addEntity(plowhead);
						}
					}
				}

				this.world.setEntityState(this, (byte) 3);
				this.remove();
			}
		}
	}
}
