package random.wings.entity.monster;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import random.wings.WingsSounds;
import random.wings.entity.TameableDragonEntity;
import random.wings.entity.WingsEntities;
import random.wings.entity.item.PlowheadEggEntity;
import random.wings.item.WingsItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class IcyPlowheadEntity extends TameableDragonEntity {
	//private static final Ingredient TEMPTATIONS = Ingredient.fromItems(WingsItems.GLISTENING_GLACIAL_SHRIMP);
	private static final DataParameter<Optional<BlockPos>> ICE_BLOCK = EntityDataManager.createKey(IcyPlowheadEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final EntitySize SLEEPING_SIZE = EntitySize.flexible(1.2f, 0.5f);
	private int alarmedTimer;
	private int attackCooldown;
	private Vec3d oldPos;
	private int ramTime;
	private RayTraceResult target;
	private BlockPos sleepTarget;
	private int startedCharging;

	public IcyPlowheadEntity(EntityType<? extends IcyPlowheadEntity> type, World worldIn) {
		super(type, worldIn);
		this.setPathPriority(PathNodeType.WATER, 1.0F);
		this.moveController = new MoveHelperController(this);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(ICE_BLOCK, Optional.empty());
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1, 40));
		this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 15, 1) {
			@Override
			public boolean shouldExecute() {
				boolean execute = super.shouldExecute();
				if (execute && closestEntity instanceof PlayerEntity && getDistanceSq(closestEntity) <= 10 && !((PlayerEntity) closestEntity).isCreative()) {
					this.entity.setAttackTarget((PlayerEntity) closestEntity);
				}
				return execute;
			}
		});
		this.goalSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> entity == getAttackTarget()));
	}

	protected PathNavigator createNavigator(World worldIn) {
		return new SwimmerPathNavigator(this, worldIn);
	}

	@Override
	public void tick() {
		if (this.inWater) {
			this.setAir(300);
		}
		super.tick();
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(isTamed() ? 44 : 30);
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		this.setGender(rand.nextBoolean());
		return spawnDataIn;
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return isSleeping() ? SLEEPING_SIZE : super.getSize(poseIn);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return WingsSounds.PLOWHEAD_AMBIENT;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return WingsSounds.PLOWHEAD_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return WingsSounds.PLOWHEAD_DEATH;
	}

	@Override
	public boolean isSilent() {
		return isSleeping() || super.isSilent();
	}

	@Override
	protected float getSoundVolume() {
		return 0.15f;
	}

	@Override
	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(isTamed() ? 44 : 30);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.getTrueSource() instanceof LivingEntity && (!(source.getTrueSource() instanceof PlayerEntity) || (!isOwner((LivingEntity) source.getTrueSource()) && !((PlayerEntity) source.getTrueSource()).abilities.isCreativeMode))) {
			Predicate<IcyPlowheadEntity> canAttack = entity -> !entity.isChild() && entity.getGender() && !entity.isOwner((LivingEntity) source.getTrueSource());
			if (canAttack.test(this)) {
				setAttackTarget((LivingEntity) source.getTrueSource());
			}
			world.getEntitiesWithinAABB(getClass(), getBoundingBox().grow(31)).stream().filter(canAttack).forEach(e -> e.setAttackTarget((LivingEntity) source.getTrueSource()));
		}

		if (isSleeping()) {
			oldPos = getPositionVec();
			alarmedTimer = 200;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote && !this.isTamed() && stack.getItem() == WingsItems.GLISTENING_GLACIAL_SHRIMP) {
			if (!player.abilities.isCreativeMode) stack.shrink(1);
			if (this.rand.nextInt(10) == 0) {
				this.setTamedBy(player);
				this.setAttackTarget(null);
				this.playTameEffect(true);
				this.world.setEntityState(this, (byte) 7);
			} else {
				this.playTameEffect(false);
				this.world.setEntityState(this, (byte) 6);
			}
		}

		if (stack.getItem() instanceof SpawnEggItem && ((SpawnEggItem) stack.getItem()).hasType(stack.getTag(), this.getType())) {
			if (!this.world.isRemote) {
				IcyPlowheadEntity plowhead = WingsEntities.ICY_PLOWHEAD.create(world);
				if (plowhead != null) {
					plowhead.setGrowingAge(-24000);
					plowhead.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
					plowhead.onInitialSpawn(world, world.getDifficultyForLocation(plowhead.getPosition()), SpawnReason.SPAWN_EGG, null, null);
					this.world.addEntity(plowhead);
					if (stack.hasDisplayName()) {
						plowhead.setCustomName(stack.getDisplayName());
					}
					if (!player.abilities.isCreativeMode) {
						stack.shrink(1);
					}
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == WingsItems.GLACIAL_SHRIMP;
	}

	@Override
	public void livingTick() {
		if (oldPos != null) {
			setPositionAndRotation(oldPos.x, oldPos.y, oldPos.z, 0, 0);
			oldPos = null;
		}
		if (!isSleeping()) {
			if (!world.isRemote) {
				LivingEntity attackTarget = getAttackTarget();
				if (attackTarget != null && attackTarget.isAlive() && attackTarget instanceof PlayerEntity && !((PlayerEntity) attackTarget).abilities.isCreativeMode) {
					getNavigator().tryMoveToEntityLiving(attackTarget, 1.2);
					if (attackCooldown == 0 && getDistanceSq(attackTarget) < 4) {
						attackEntityAsMob(attackTarget);
						attackCooldown = 20;
					}
				} else if (attackCooldown > 0) {
					getNavigator().clearPath();
					attackCooldown = 0;
					setAttackTarget(null);
				}
				Optional<BlockPos> iceBlock = this.dataManager.get(ICE_BLOCK);
				if (target != null) {
					if (startedCharging == 0) {
						playSound(WingsSounds.PLOWHEAD_ANGRY, getSoundVolume(), getSoundPitch());
						startedCharging = 120;
					}
					setMotion(MathHelper.clamp(target.getHitVec().x - getPosX(), -0.5, 0.5), MathHelper.clamp(target.getHitVec().y - getPosY(), -0.3, 0.3), MathHelper.clamp(target.getHitVec().z - getPosZ(), -0.5, 0.5));
					getLookController().setLookPosition(target.getHitVec().x, target.getHitVec().y, target.getHitVec().z, 0, 0);
					if (getDistanceSq(target.getHitVec()) <= 4) {
						switch (target.getType()) {
							case BLOCK:
								world.removeBlock(((BlockRayTraceResult) target).getPos(), false);
								break;
							case ENTITY:
								attackEntityAsMob(((EntityRayTraceResult) target).getEntity());
								break;
						}
						target = null;
						startedCharging = 0;
					}
				} else {
					iceBlock.ifPresent(it -> {
						if (startedCharging == 0) {
							playSound(WingsSounds.PLOWHEAD_ANGRY, getSoundVolume(), getSoundPitch());
							startedCharging = 120;
						} else if (startedCharging == 1) {
							dataManager.set(ICE_BLOCK, Optional.empty());
							return;
						} else --startedCharging;
						setMotion(MathHelper.clamp(it.getX() - getPosX(), -0.5, 0.5), MathHelper.clamp(it.getY() - getPosY(), -0.3, 0.3), MathHelper.clamp(it.getZ() - getPosZ(), -0.5, 0.5));
						for (Entity entity : world.getEntitiesInAABBexcluding(this, getBoundingBox().grow(1), entity -> entity instanceof LivingEntity)) {
							entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
						}
						getLookController().setLookPosition(it.getX(), it.getY(), it.getZ(), 0, 0);
						if (it.distanceSq(getPosX(), getPosY(), getPosZ(), true) <= 4) {
							BlockState state = world.getBlockState(it);
							world.setEntityState(this, (byte) 5);
							if (state.isSolid()) {
								for (ItemStack drop : state.getDrops(new LootContext.Builder((ServerWorld) world).withRandom(rand))) {
									entityDropItem(drop, (float) (it.getY() - getPosY()));
								}
							}
							world.removeBlock(it, false);
							dataManager.set(ICE_BLOCK, Optional.empty());
							ramTime = rand.nextInt(1200) + 1200;
							startedCharging = 0;
						}
					});
				}
				if (attackCooldown-- <= 0) attackCooldown = 0;
				if (alarmedTimer-- <= 0) alarmedTimer = 0;
				if (ramTime-- <= 0) ramTime = 0;
				if (ramTime == 0 && !iceBlock.isPresent() && ticksExisted % 20 == 0) {
					List<BlockPos> possible = new ArrayList<>();
					BlockPos.getAllInBox(getPosition().add(-16, -16, -16), getPosition().add(16, 16, 16)).forEach(pos -> {
						BlockState state = world.getBlockState(pos);
						Material material = state.getMaterial();
						if (material == Material.ICE || material == Material.PACKED_ICE) {
							possible.add(pos.toImmutable());
						}
					});
					if (possible.size() > 0)
						dataManager.set(ICE_BLOCK, Optional.of(possible.get(rand.nextInt(possible.size()))));
				}
			}
			super.livingTick();
		} else this.travel(new Vec3d(this.moveStrafing, this.moveVertical, this.moveForward));
	}

	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 5) {
			dataManager.get(ICE_BLOCK).ifPresent(iceBlock -> {
				IParticleData data = new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(iceBlock)).setPos(iceBlock);
				for (int i = 0; i < 7; ++i) {
					double d0 = this.rand.nextGaussian() * 0.01D;
					double d1 = this.rand.nextGaussian() * 0.01D;
					double d2 = this.rand.nextGaussian() * 0.01D;
					this.world.addParticle(data, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.2D, this.getPosZRandom(1.0D), d0, d1, d2);
				}
			});
		} else super.handleStatusUpdate(id);
	}

	@Override
	public boolean isSleeping() {
		boolean isNight = world.getDayTime() > 13000 && world.getDayTime() < 23000;
		boolean ground = world.getBlockState(new BlockPos(getPosX(), getPosY() - 1, getPosZ())).getBlock() != Blocks.WATER;
		if (isNight) {
			if (!ground) {
				if (sleepTarget == null) {
					BlockPos p = getPosition().add(rand.nextInt(64) - 32, -1, rand.nextInt(64) - 32);
					while (world.getBlockState(p).getBlock() == Blocks.WATER) {
						p = p.down();
					}
					sleepTarget = p;
				}
				setMotion(MathHelper.clamp(sleepTarget.getX() - getPosX(), -0.1, 0.1), MathHelper.clamp(sleepTarget.getY() - getPosY(), -0.2, 0.2), MathHelper.clamp(sleepTarget.getZ() - getPosZ(), -0.03, 0.03));
				return false;
			}
			return alarmedTimer == 0 && ground;
		} else if (sleepTarget != null) {
			sleepTarget = null;
			setMotion(getMotion().add(0, 0.2, 0));
		}
		return false;
	}

	@Override
	public void createEgg() {
		world.addEntity(new PlowheadEggEntity(world, this));
	}

	@Override
	public ItemStack getEgg() {
		return new ItemStack(WingsItems.ICY_PLOWHEAD_EGG);
	}

	public void setTarget(RayTraceResult target) {
		this.target = target;
	}

	public void travel(Vec3d p_213352_1_) {
		if (this.isServerWorld() && this.isInWater()) {
			this.moveRelative(0.1F, p_213352_1_);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.9D));
			if (this.collidedHorizontally) {
				this.setMotion(this.getMotion().x, 0.1F, this.getMotion().z);
			}
			if (this.getAttackTarget() == null) {
				this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(p_213352_1_);
		}
	}

	static class MoveHelperController extends MovementController {
		private final IcyPlowheadEntity plowhead;

		MoveHelperController(IcyPlowheadEntity plowhead) {
			super(plowhead);
			this.plowhead = plowhead;
		}

		public void tick() {
			if (this.plowhead.areEyesInFluid(FluidTags.WATER)) {
				this.plowhead.setMotion(this.plowhead.getMotion().add(0.0D, 0.005D, 0.0D));
			}

			if (this.action == MovementController.Action.MOVE_TO && !this.plowhead.getNavigator().noPath()) {
				double d0 = this.posX - this.plowhead.getPosX();
				double d1 = this.posY - this.plowhead.getPosY();
				double d2 = this.posZ - this.plowhead.getPosZ();
				double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
				d1 = d1 / d3;
				float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
				this.plowhead.rotationYaw = this.limitAngle(this.plowhead.rotationYaw, f, 90.0F);
				this.plowhead.renderYawOffset = this.plowhead.rotationYaw;
				float f1 = (float) (this.speed * this.plowhead.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
				this.plowhead.setAIMoveSpeed(MathHelper.lerp(0.125F, this.plowhead.getAIMoveSpeed(), f1));
				this.plowhead.setMotion(this.plowhead.getMotion().add(0.0D, (double) this.plowhead.getAIMoveSpeed() * d1 * 0.1D, 0.0D));
			} else {
				this.plowhead.setAIMoveSpeed(0.0F);
			}
		}
	}
}
