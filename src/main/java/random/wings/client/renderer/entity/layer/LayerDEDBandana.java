package random.wings.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.entity.DumpyEggDrakeRenderer;
import random.wings.client.renderer.entity.model.DumpyEggDrakeModel;
import random.wings.entity.passive.DumpyEggDrakeEntity;

import java.util.HashMap;
import java.util.Map;

public class LayerDEDBandana extends LayerRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final Map<Integer, ResourceLocation> TEXTURES = new HashMap<>();

    public LayerDEDBandana(DumpyEggDrakeRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DumpyEggDrakeEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible()) {
            DyeColor color = entitylivingbaseIn.getBandanaColor();
            this.getEntityModel().render(matrixStackIn, bufferIn.getBuffer(this.getEntityModel().getRenderType(TEXTURES.computeIfAbsent((color.getId() << 1) | (entitylivingbaseIn.isChild() ? 1 : 0), k -> new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/dumpy_egg_drake/bandana/" + ((k & 1) > 0 ? "baby_" : "") + "bandana_" + DyeColor.byId(k >> 1) + ".png")))), packedLightIn, 0, 1, 1, 1, 1);
        }
    }
}
