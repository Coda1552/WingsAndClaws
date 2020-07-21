package net.msrandom.wings.client.renderer.entity.layer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.DumpyEggDrakeRenderer;
import net.msrandom.wings.client.renderer.entity.model.DumpyEggDrakeModel;
import net.msrandom.wings.entity.passive.DumpyEggDrakeEntity;

public class FeatureDEDBandana extends FeatureRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final Identifier[] TEXTURES = new Identifier[32];

    public FeatureDEDBandana(DumpyEggDrakeRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, DumpyEggDrakeEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible()) {
            DyeColor color = entitylivingbaseIn.getBandanaColor();
            int texture = (color.getId() << 1) | (entitylivingbaseIn.isBaby() ? 1 : 0);
            Identifier resource = TEXTURES[texture];
            if (resource == null)
                TEXTURES[texture] = resource = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/dumpy_egg_drake/bandana/" + (entitylivingbaseIn.isBaby() ? "baby_" : "") + "bandana_" + color + ".png");
            this.getContextModel().render(matrixStackIn, bufferIn.getBuffer(this.getContextModel().getLayer(resource)), packedLightIn, OverlayTexture.packUv(OverlayTexture.getU(0), OverlayTexture.getV(entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0)), 1, 1, 1, 1);
        }
    }
}
