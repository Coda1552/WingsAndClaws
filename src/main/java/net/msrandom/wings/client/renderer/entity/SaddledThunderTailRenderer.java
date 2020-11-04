package net.msrandom.wings.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.SaddledThunderTailModel;
import net.msrandom.wings.entity.passive.SaddledThunderTailEntity;

public class SaddledThunderTailRenderer extends MobRenderer<SaddledThunderTailEntity, SaddledThunderTailModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[8];
    private final SaddledThunderTailModel adult;
    private final SaddledThunderTailModel child;

    public SaddledThunderTailRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SaddledThunderTailModel.Adult(), 1.2f);
        adult = entityModel;
        child = new SaddledThunderTailModel.Child();
    }

    @Override
    public void render(SaddledThunderTailEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityModel = entityIn.isChild() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void preRenderCallback(SaddledThunderTailEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);

    }

    @Override
    public ResourceLocation getEntityTexture(SaddledThunderTailEntity entity) {
        byte texture = 0;
        if (entity.isChild()) texture |= 1;
        if (entity.isSleeping()) texture |= 4;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s%s", ((texture & 1) != 0) ? "child" : "adult", ((texture & 4) != 0) ? "_sleep" : "");
            ResourceLocation location = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/saddled_thunder_tail/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
