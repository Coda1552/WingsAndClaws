package net.msrandom.wings.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.msrandom.wings.client.WingsSounds;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.WingsEntities;
import net.msrandom.wings.entity.item.PlowheadEggEntity;
import net.msrandom.wings.item.WingsItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IcyPlowheadEntity extends TameableDragonEntity {
	//private static final Ingredient TEMPTATIONS = Ingredient.fromItems(WingsItems.GLISTENING_GLACIAL_SHRIMP);
	private static final EntitySize SLEEPING_SIZE = EntitySize.flexible(1.2f, 0.5f);
	private final Map<ToolType, ItemStack> tools = new HashMap<>();
	private ItemStack horn = ItemStack.EMPTY;
	private BlockPos iceBlock;
	public float pitch;
	private int alarmedTimer;
	private boolean attacking;
	private Vector3d oldPos;
	private int ramTime;
	private BlockPos target;
	private BlockPos sleepTarget;
	private int startedCharging;

	public IcyPlowheadEntity(EntityType<? extends IcyPlowheadEntity> type, World worldIn) {
		super(type, worldIn);
		this.moveController = new MoveHelperController(this);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 1, 40) {
			@Override
			public boolean shouldExecute() {
				return super.shouldExecute() && getState() == WanderState.WANDER;
			}
		});
		this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
	}

	@Override
	public void tick() {
		this.setAir(300);
		super.tick();
	}

	protected PathNavigator createNavigator(World worldIn) {
		return new SwimmerPathNavigator(this, worldIn);
	}

    public static AttributeModifierMap.MutableAttribute registerPlowheadAttributes() {
        return LivingEntity.registerAttributes().createMutableAttribute(Attributes.FOLLOW_RANGE, 16).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2).createMutableAttribute(Attributes.MAX_HEALTH, 30).createMutableAttribute(Attributes.ATTACK_DAMAGE, 3).createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 3);
    }

	@Override
	public EntitySize getSize(Pose poseIn) {
		return isSleeping() ? SLEEPING_SIZE : super.getSize(poseIn);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return isSleeping() ? null : WingsSounds.PLOWHEAD_AMBIENT;
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
	protected float getSoundVolume() {
		return 0.15f;
	}

	@Override
	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(isTamed() ? 44 : 30);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.getTrueSource() instanceof LivingEntity && (!(source.getTrueSource() instanceof PlayerEntity) || (!isOwner((LivingEntity) source.getTrueSource()) && !((PlayerEntity) source.getTrueSource()).abilities.isCreativeMode))) {
			if (!isChild() && !isOwner((LivingEntity) source.getTrueSource()))
				setAttackTarget((LivingEntity) source.getTrueSource());
		}

		if (isSleeping()) {
			oldPos = getPositionVec();
			alarmedTimer = 200;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote && !this.isTamed() && stack.getItem() == WingsItems.GLISTENING_GLACIAL_SHRIMP) {
			if (!player.abilities.isCreativeMode) stack.shrink(1);
			if (this.rand.nextInt(3) == 0) {
				this.setTamedBy(player);
				this.setAttackTarget(null);
				this.world.setEntityState(this, (byte) 7);
			} else {
				this.world.setEntityState(this, (byte) 6);
			}
		}

		if (stack.getItem() instanceof SpawnEggItem && ((SpawnEggItem) stack.getItem()).hasType(stack.getTag(), this.getType())) {
			if (!this.world.isRemote) {
				IcyPlowheadEntity plowhead = WingsEntities.ICY_PLOWHEAD.create(world);
				if (plowhead != null) {
					plowhead.setGrowingAge(-24000);
					plowhead.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
					plowhead.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(plowhead.getPosition()), SpawnReason.SPAWN_EGG, null, null);
					this.world.addEntity(plowhead);
					if (stack.hasDisplayName()) {
						plowhead.setCustomName(stack.getDisplayName());
					}
					if (!player.abilities.isCreativeMode) {
						stack.shrink(1);
					}
				}
			}

			return ActionResultType.SUCCESS;
		}
		return super.func_230254_b_(player, hand);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == WingsItems.GLACIAL_SHRIMP;
	}

	@Override
	public void livingTick() {
		if (oldPos != null) {
			setPosition(oldPos.x, oldPos.y, oldPos.z);
			oldPos = null;
		}
		if (!isSleeping()) {
			if (this.inWater) {
				this.pitch = (float) MathHelper.clampedLerp(this.pitch, -(this.getMotion().getY() * 180), MathHelper.sin(ticksExisted) * 2);
			} else {
				this.pitch = 0;
			}

			if (!world.isRemote) {
				if (isTamed()) {
					if (getState() == WanderState.FOLLOW) {
						LivingEntity owner = getOwner();
						if (owner != null) {
							getNavigator().tryMoveToEntityLiving(owner, 0.2);
							if (onGround) {
								double x = owner.getPosX() - getPosX();
								double z = owner.getPosZ() - getPosZ();
								setMotion(MathHelper.clamp(x, -0.2, 0.2), 0, MathHelper.clamp(z, -0.2, 0.2));

								rotationYaw = (float) Math.toDegrees(Math.atan2(z, x) - Math.PI / 2);
								renderYawOffset = rotationYaw;
							}
						}
					}
				} else {
					LivingEntity attackTarget = getAttackTarget();

					if (attackTarget == null) {
						PlayerEntity player = world.getClosestPlayer(this, 16);
						if (player != null && !player.abilities.isCreativeMode) {
							setAttackTarget(player);
							attacking = true;
						}
					}

					if (attackTarget != null && attackTarget.isAlive()) {
						getNavigator().tryMoveToEntityLiving(attackTarget, 0.4);
						if (getDistanceSq(attackTarget) <= 4) {
							attackEntityAsMob(attackTarget);
						}
					} else if (attacking) {
						getNavigator().clearPath();
						attacking = false;
						setAttackTarget(null);
					}
				}

				if (target != null) {
					if (startedCharging == 0) {
						playSound(WingsSounds.PLOWHEAD_ANGRY, getSoundVolume(), getSoundPitch());
						startedCharging = 120;
					}
					getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), 0.4);

					double speed = getMotion().x * getMotion().x + getMotion().y * getMotion().y + getMotion().z + getMotion().z;
					if (speed > 0.05) {
						for (Entity entity : world.getEntitiesInAABBexcluding(this, getBoundingBox().grow(1), entity -> entity instanceof LivingEntity)) {
							entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
						}
					}

					if (collidedHorizontally) {
						breakBlock(new BlockPos(getPositionVec().add(getMotion())), false);
						target = null;
					} else if (getDistanceSq(target.getX(), target.getY(), target.getZ()) <= 4) {
						breakBlock(target, true);
						startedCharging = 0;
						target = null;
					}
				} else {
					if (iceBlock != null) {
						if (startedCharging == 0) {
							playSound(WingsSounds.PLOWHEAD_ANGRY, getSoundVolume(), getSoundPitch());
							startedCharging = 120;
						} else if (startedCharging == 1) {
							iceBlock = null;
							return;
						} else --startedCharging;
						getNavigator().tryMoveToXYZ(iceBlock.getX(), iceBlock.getY(), iceBlock.getZ(), 0.4);

						double speed = getMotion().x * getMotion().x + getMotion().y * getMotion().y + getMotion().z + getMotion().z;
						if (speed > 0.05) {
							for (Entity entity : world.getEntitiesInAABBexcluding(this, getBoundingBox().grow(1), entity -> entity instanceof LivingEntity)) {
								entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
							}
						}
						if (iceBlock.distanceSq(getPosX(), getPosY(), getPosZ(), true) <= 4) {
							breakBlock(iceBlock, false);
							iceBlock = null;
							ramTime = rand.nextInt(1200) + 1200;
							startedCharging = 0;
						}
					}
				}

				if (alarmedTimer-- <= 0) alarmedTimer = 0;
				if ((ramTime <= 0 || --ramTime == 0) && iceBlock == null && ticksExisted % 20 == 0) {
					List<BlockPos> possible = new ArrayList<>();
					BlockPos start = getPosition();
					Vector3d vec3d = getPositionVec();
					BlockPos.Mutable mutable = start.toMutable();
					BlockPos.getAllInBox(start.add(-16, -16, -16), start.add(16, 16, 16)).forEach(pos -> {
						Material material = world.getBlockState(pos).getMaterial();
						if (material == Material.ICE || material == Material.PACKED_ICE) {
							BlockRayTraceResult rayTrace = this.world.rayTraceBlocks(new RayTraceContext(vec3d, Vector3d.copyCentered(pos), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
							if (rayTrace.getType() != RayTraceResult.Type.MISS && rayTrace.getPos().equals(pos)) {
								BlockPos.Mutable p = mutable.setPos(pos);
								boolean flag = world.getFluidState(p.move(Direction.DOWN)).getFluid() == Fluids.WATER;
								for (int i = 2; i < Direction.values().length; ++i) {
									flag |= world.getFluidState(p.move(Direction.values()[i])).getFluid() == Fluids.WATER;
									if (flag) {
										possible.add(pos.toImmutable());
										break;
									}
								}
							}
						}
					});
					if (possible.size() > 0)
						iceBlock = possible.get(rand.nextInt(possible.size()));
				}
			}
			super.livingTick();
		} else this.travel(new Vector3d(this.moveStrafing, this.moveVertical, this.moveForward));
	}

	@Override
	public boolean isSleeping() {
		if (world.getDayTime() > 13000 && world.getDayTime() < 23000) {
			if (world.getBlockState(new BlockPos(getPosX(), getPosY() - 1, getPosZ())).getBlock() == Blocks.WATER) {
				if (sleepTarget == null) {
					BlockPos p = getPosition().add(rand.nextInt(64) - 32, -1, rand.nextInt(64) - 32);
					while (world.getBlockState(p).getBlock() == Blocks.WATER) {
						p = p.down();
					}
					sleepTarget = p;
				}
				getNavigator().tryMoveToXYZ(sleepTarget.getX(), sleepTarget.getY(), sleepTarget.getZ(), 0.2);

				return false;
			}
			return alarmedTimer == 0;
		} else if (sleepTarget != null) {
			sleepTarget = null;
			setMotion(getMotion().add(0, 0.2, 0));
		}
		return false;
	}

	private void breakBlock(BlockPos pos, boolean selected) {
		if (eyesInWater) {
			BlockState state = world.getBlockState(pos);
			world.playEvent(2001, pos, Block.getStateId(state));
			Material material = state.getMaterial();
			if (material == Material.ICE || material == Material.PACKED_ICE) {
				if (!selected || rand.nextBoolean())
					entityDropItem(new ItemStack(WingsItems.GLACIAL_SHRIMP), (float) (pos.getY() - getPosY()));
			} else if (state.isSolid()) {
				ToolType type = state.getHarvestTool();
				if (type == null) type = ToolType.PICKAXE;
				ItemStack stack = tools.computeIfAbsent(type, k -> {
					if (k == ToolType.AXE) return new ItemStack(Items.IRON_AXE);
					if (k == ToolType.PICKAXE) return new ItemStack(Items.IRON_PICKAXE);
					if (k == ToolType.SHOVEL) return new ItemStack(Items.IRON_SHOVEL);
					return ItemStack.EMPTY;
				});
				setupTool(stack);
				if (selected) {
					for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
						if (entry.getKey() == Enchantments.EFFICIENCY) {
							List<BlockPos> positions = BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1)).map(BlockPos::toImmutable).collect(Collectors.toList());
							int k = 0;
							for (int i = 0; i < entry.getValue(); i++) {
								for (int i1 = k; i1 < positions.size(); i1++) {
									BlockPos p = positions.get(i1);
									if (world.getBlockState(p).isSolid()) {
										breakBlock(p, false);
										k = i1;
										break;
									}
								}
							}
							break;
						}
					}
				}

				for (ItemStack drop : state.getDrops(new LootContext.Builder((ServerWorld) world).withRandom(rand).withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(pos)).withParameter(LootParameters.TOOL, stack))) {
					entityDropItem(drop, (float) (pos.getY() - getPosY()));
				}
			}
			world.removeBlock(pos, false);
		}
	}

	private void setupTool(ItemStack stack) {
		if (!horn.isEmpty()) {
			for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(horn).entrySet())
				stack.addEnchantment(entry.getKey(), entry.getValue());
			horn = ItemStack.EMPTY;
		}
	}

	@Override
	public void createEgg() {
		world.addEntity(new PlowheadEggEntity(world, getPosX(), getPosY(), getPosZ()));
	}

	@Override
	public ItemStack getEgg() {
		return new ItemStack(WingsItems.ICY_PLOWHEAD_EGG);
	}

	public void setTarget(RayTraceResult target) {
		if (target.getType() == RayTraceResult.Type.ENTITY) {
			Entity entity = ((EntityRayTraceResult) target).getEntity();
			if (entity instanceof LivingEntity) setAttackTarget((LivingEntity) entity);
		} else if (target.getType() == RayTraceResult.Type.BLOCK) this.target = ((BlockRayTraceResult) target).getPos();
	}

	@Override
	public void travel(Vector3d p_213352_1_) {
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

	public void setHorn(ItemStack horn) {
		this.horn = horn;
	}

	private static class MoveHelperController extends MovementController {
		private final IcyPlowheadEntity plowhead;

		MoveHelperController(IcyPlowheadEntity plowhead) {
			super(plowhead);
			this.plowhead = plowhead;
		}

		public void tick() {
			this.plowhead.setMotion(this.plowhead.getMotion().add(0.0D, 0.005D, 0.0D));
			if (this.action == Action.MOVE_TO && !this.plowhead.getNavigator().noPath()) {
				double d0 = this.posX - this.plowhead.getPosX();
				double d1 = this.posY - this.plowhead.getPosY();
				double d2 = this.posZ - this.plowhead.getPosZ();
				double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
				d1 = d1 / d3;
				float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
				this.plowhead.rotationYaw = this.limitAngle(this.plowhead.rotationYaw, f, 90.0F);
				this.plowhead.renderYawOffset = this.plowhead.rotationYaw;
				float f1 = (float) (this.speed * this.plowhead.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
				this.plowhead.setAIMoveSpeed(MathHelper.lerp(0.125F, this.plowhead.getAIMoveSpeed(), f1));
				this.plowhead.setMotion(this.plowhead.getMotion().add(MathHelper.clamp(d0, speed / -10, speed / 10), (double) this.plowhead.getAIMoveSpeed() * d1 * 0.1D, MathHelper.clamp(d2, speed / -10, speed / 10)));
			} else {
				this.plowhead.setAIMoveSpeed(0.0F);
			}
		}
	}
}
