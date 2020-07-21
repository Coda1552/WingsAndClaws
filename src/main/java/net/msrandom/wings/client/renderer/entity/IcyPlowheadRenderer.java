package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.IcyPlowheadModel;
import net.msrandom.wings.entity.monster.IcyPlowheadEntity;

public class IcyPlowheadRenderer extends MobEntityRenderer<IcyPlowheadEntity, IcyPlowheadModel> {
    private static final Identifier[] TEXTURES = new Identifier[8];
    private final IcyPlowheadModel adult;
    private final IcyPlowheadModel child;

    public IcyPlowheadRenderer(EntityRenderDispatcher p_i50961_1_) {
        super(p_i50961_1_, new IcyPlowheadModel.Adult(), 0.75f);
        adult = model;
        child = new IcyPlowheadModel.Child();
    }

    @Override
    public void render(IcyPlowheadEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        model = entityIn.isBaby() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void scale(IcyPlowheadEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(entitylivingbaseIn.pitch));
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    public Identifier getTexture(IcyPlowheadEntity entity) {
        byte texture = 0;
        if (entity.isBaby()) texture |= 1;
        if (entity.getGender()) texture |= 2;
        if (entity.isSleeping()) texture |= 4;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s_%s%s", ((texture & 1) != 0) ? "child" : "adult", ((texture & 2) != 0) ? "male" : "female", ((texture & 4) != 0) ? "_sleep" : "");
            Identifier location = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
