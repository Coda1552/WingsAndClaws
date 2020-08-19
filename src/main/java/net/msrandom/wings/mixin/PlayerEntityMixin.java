package net.msrandom.wings.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.msrandom.wings.item.WingsItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.isInvulnerableTo(source) && player.isUsingItem()) {
            ItemStack stack = player.getActiveItem();
            if (stack.getItem() == WingsItems.ICY_PLOWHEAD_SHIELD) {
                double speed = Math.abs(player.getVelocity().x * player.getVelocity().x + player.getVelocity().z * player.getVelocity().z);
                if (!player.abilities.creativeMode)
                    stack.damage(1, player, entity -> entity.sendToolBreakStatus(player.getActiveHand()));
                Entity entity = source.getAttacker();
                if (entity != null && !source.isUnblockable()) {
                    Vec3d vec3d = source.getPosition();
                    if (vec3d != null) {
                        Vec3d vec3d1 = player.getRotationVec(1.0F);
                        Vec3d vec3d2 = vec3d.reverseSubtract(player.getPos()).normalize();
                        vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
                        if (vec3d2.dotProduct(vec3d1) < 0.0D) {
                            if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).takeKnockback((float) speed * 3.325f, MathHelper.sin((float) Math.toRadians(-entity.yaw)), MathHelper.cos((float) Math.toRadians(entity.yaw)));
                            } else {
                                double strength = speed * 3.325;
                                Vec3d vec3d3 = entity.getVelocity();
                                Vec3d vec3d4 = new Vec3d(MathHelper.sin((float) Math.toRadians(-entity.yaw)), 0.0D, MathHelper.cos((float) Math.toRadians(entity.yaw))).normalize().multiply(strength);
                                entity.setVelocity(vec3d3.x / 2.0D - vec3d4.x, entity.isOnGround() ? Math.min(0.4D, vec3d3.y / 2.0D + strength) : vec3d3.y, vec3d3.z / 2.0D - vec3d4.z);
                            }
                            cir.setReturnValue(false);
                        }
                    }
                }
            }
        }
    }
}
