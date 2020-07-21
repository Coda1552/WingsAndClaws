package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.layer.FeatureDEDBandana;
import net.msrandom.wings.client.renderer.entity.model.DumpyEggDrakeModel;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;

public class DumpyEggDrakeRenderer extends MobEntityRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final Identifier[] TEXTURES = new Identifier[8];
    private final DumpyEggDrakeModel adult;
    private final DumpyEggDrakeModel child;

    public DumpyEggDrakeRenderer(EntityRenderDispatcher p_i50961_1_) {
        super(p_i50961_1_, new DumpyEggDrakeModel.Adult(), 0.5f);
        adult = model;
        child = new DumpyEggDrakeModel.Child();
        addFeature(new FeatureDEDBandana(this));
    }

    @Override
    public void render(DumpyEggDrakeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        model = entityIn.isBaby() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void scale(DumpyEggDrakeEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
        if (entitylivingbaseIn.isSleeping())
            matrixStackIn.translate(0, entitylivingbaseIn.isBaby() ? 0.325 : 0.65, 0);
    }

    @Override
    public Identifier getTexture(DumpyEggDrakeEntity entity) {
        byte texture = 0;
        if (entity.isBaby()) texture |= 1;
        if (entity.getGender()) texture |= 2;
        if (entity.isSleeping()) texture |= 4;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s_%s%s", ((texture & 1) != 0) ? "child" : "adult", ((texture & 2) != 0) ? "male" : "female", ((texture & 4) != 0) ? "_sleep" : "");
            Identifier location = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/dumpy_egg_drake/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
