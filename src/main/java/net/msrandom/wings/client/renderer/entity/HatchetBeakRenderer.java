package net.msrandom.wings.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.layer.LayerHatchetBeakSaddle;
import net.msrandom.wings.client.renderer.entity.model.HatchetBeakModel;
import net.msrandom.wings.entity.TameableDragonEntity;
import net.msrandom.wings.entity.monster.HatchetBeakEntity;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

public class HatchetBeakRenderer extends MobRenderer<HatchetBeakEntity, HatchetBeakModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[4];

    public HatchetBeakRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new HatchetBeakModel(), 0.75f);
        addLayer(new LayerHatchetBeakSaddle(this));
    }

    @Override
    protected void preRenderCallback(HatchetBeakEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
        matrixStackIn.translate(0, 0.0625, 0);
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
