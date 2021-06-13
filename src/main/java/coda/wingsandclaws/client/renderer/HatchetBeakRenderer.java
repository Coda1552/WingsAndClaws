package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.renderer.layer.HatchetBeakSaddleLayer;
import coda.wingsandclaws.client.model.HatchetBeakModel;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.entity.HatchetBeakEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import coda.wingsandclaws.WingsAndClaws;

public class HatchetBeakRenderer extends MobRenderer<HatchetBeakEntity, HatchetBeakModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[4];

    public HatchetBeakRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new HatchetBeakModel(), 0.75f);
        addLayer(new HatchetBeakSaddleLayer(this));
    }

    @Override
    protected void preRenderCallback(HatchetBeakEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
        matrixStackIn.translate(0, -0.5, 0);
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTickTime, entitylivingbaseIn.prevTilt, entitylivingbaseIn.tilt)));
    }

    @Override
    public ResourceLocation getEntityTexture(HatchetBeakEntity entity) {
        byte texture = 0;
        if (entity.getGender() == TameableDragonEntity.Gender.MALE) texture |= 1;
        if (entity.isSleeping()) texture |= 2;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s%s", ((texture & 1) != 0) ? "male" : "female", ((texture & 2) != 0) ? "_sleep" : "");
            ResourceLocation location = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/hatchet_beak/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
