package net.msrandom.wings.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.DumpyEggDrakeRenderer;
import net.msrandom.wings.client.renderer.entity.model.DumpyEggDrakeModel;
import net.msrandom.wings.entity.monster.DumpyEggDrakeEntity;

public class LayerDEDBandana extends LayerRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[32];

    public LayerDEDBandana(DumpyEggDrakeRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DumpyEggDrakeEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible()) {
            DyeColor color = entitylivingbaseIn.getBandanaColor();
            int texture = (color.getId() << 1) | (entitylivingbaseIn.isChild() ? 1 : 0);
            ResourceLocation resource = TEXTURES[texture];
            if (resource == null)
                TEXTURES[texture] = resource = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/dumpy_egg_drake/bandana/" + (entitylivingbaseIn.isChild() ? "baby_" : "") + "bandana_" + color + ".png");
            this.getEntityModel().render(matrixStackIn, bufferIn.getBuffer(this.getEntityModel().getRenderType(resource)), packedLightIn, OverlayTexture.getPackedUV(OverlayTexture.getU(0), OverlayTexture.getV(entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0)), 1, 1, 1, 1);
        }
    }
}
