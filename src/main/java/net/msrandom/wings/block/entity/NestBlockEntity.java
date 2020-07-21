package net.msrandom.wings.block.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;

public abstract class NestBlockEntity extends BlockEntity implements Tickable {
    public NestBlockEntity(BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public abstract boolean addEgg();

    public abstract boolean removeEgg();

    public abstract int getEggCount();

    @Environment(EnvType.CLIENT)
    public static void render(Model model, Identifier textures, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5, 1.5, 0.5);
        matrixStackIn.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
        RenderSystem.enableRescaleNormal();
        model.render(matrixStackIn, bufferIn.getBuffer(model.getLayer(textures)), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
        matrixStackIn.pop();
    }
}
