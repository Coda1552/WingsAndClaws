package net.msrandom.wings.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.HatchetBeakModel;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

public class HatchetBeakRenderer extends MobRenderer<HatchetBeakEntity, HatchetBeakModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[8];
    private final HatchetBeakModel adult;
    private final HatchetBeakModel child;

    public HatchetBeakRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new HatchetBeakModel.Adult(), 0.75f);
        adult = entityModel;
        child = new HatchetBeakModel.Child();
    }

    @Override
    public void render(HatchetBeakEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityModel = entityIn.isChild() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void preRenderCallback(HatchetBeakEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
        matrixStackIn.translate(0, -0.28, 0);
    }

    @Override
    public ResourceLocation getEntityTexture(HatchetBeakEntity entity) {
        byte texture = 0;
        if (entity.isChild()) texture |= 1;
        if (entity.getGender()) texture |= 2;
        if (entity.isSleeping()) texture |= 4;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s_%s%s", ((texture & 1) != 0) ? "child" : "adult", ((texture & 2) != 0) ? "male" : "female", ((texture & 4) != 0) ? "_sleep" : "");
            ResourceLocation location = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/hatchet_beak/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
