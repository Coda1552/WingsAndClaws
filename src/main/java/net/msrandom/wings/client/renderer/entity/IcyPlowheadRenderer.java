package net.msrandom.wings.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.IcyPlowheadModel;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

public class IcyPlowheadRenderer extends MobRenderer<IcyPlowheadEntity, IcyPlowheadModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[4];

    public IcyPlowheadRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new IcyPlowheadModel(), 0.75f);
    }

    @Override
    protected void preRenderCallback(IcyPlowheadEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entitylivingbaseIn.pitch));
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    public ResourceLocation getEntityTexture(IcyPlowheadEntity entity) {
        byte texture = 0;
        if (entity.getGender()) texture |= 1;
        if (entity.isSleeping()) texture |= 2;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s%s", ((texture & 1) != 0) ? "male" : "female", ((texture & 2) != 0) ? "_sleep" : "");
            ResourceLocation location = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
