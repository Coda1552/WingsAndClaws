package coda.wingsandclaws.client.renderer.layer;

import coda.wingsandclaws.client.model.DumpyEggDrakeModel;
import coda.wingsandclaws.entity.DumpyEggDrakeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.client.renderer.DumpyEggDrakeRenderer;

public class DEDBandanaLayer extends LayerRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[32];

    public DEDBandanaLayer(DumpyEggDrakeRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DumpyEggDrakeEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isTame() && !entitylivingbaseIn.isInvisible()) {
            DyeColor color = entitylivingbaseIn.getBandanaColor();
            int texture = (color.getId() << 1) | (entitylivingbaseIn.isBaby() ? 1 : 0);
            ResourceLocation resource = TEXTURES[texture];
            if (resource == null)
                TEXTURES[texture] = resource = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/dumpy_egg_drake/bandana/" + (entitylivingbaseIn.isBaby() ? "baby_" : "") + "bandana_" + color + ".png");
            this.getParentModel().renderToBuffer(matrixStackIn, bufferIn.getBuffer(this.getParentModel().renderType(resource)), packedLightIn, OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0)), 1, 1, 1, 1);
        }
    }
}
