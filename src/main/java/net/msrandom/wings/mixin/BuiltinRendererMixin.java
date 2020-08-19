package net.msrandom.wings.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.msrandom.wings.client.ClientEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinRendererMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        BuiltinModelItemRenderer renderer = ClientEventHandler.BUILT_IN_MODELS.get(stack.getItem());
        if (renderer != null) {
            renderer.render(stack, mode, matrixStack, vertexConsumerProvider, i, j);
            ci.cancel();
        }
    }
}
