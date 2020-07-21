package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.MimangoModel;
import net.msrandom.wings.entity.passive.MimangoEntity;

public class MimangoRenderer extends MobEntityRenderer<MimangoEntity, MimangoModel> {
    private static final Identifier[] TEXTURES = new Identifier[6];
    private final MimangoModel adult;
    private final MimangoModel child;

    public MimangoRenderer(EntityRenderDispatcher p_i50961_1_) {
        super(p_i50961_1_, new MimangoModel.Adult(), 0.1f);
        adult = model;
        child = new MimangoModel.Baby();
    }

    @Override
    public void render(MimangoEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        model = entityIn.isBaby() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public Identifier getTexture(MimangoEntity entity) {
        int variant = entity.isBaby() ? 1 : entity.getVariant() + 2;
        Identifier texture = TEXTURES[variant - 1];
        if (texture == null) {
            TEXTURES[variant - 1] = texture = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/mimango/mimango_" + variant + ".png");
        }
        return texture;
    }
}
