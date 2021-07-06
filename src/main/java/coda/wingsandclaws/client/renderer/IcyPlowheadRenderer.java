package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.model.IcyPlowheadModel;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.entity.IcyPlowheadEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import coda.wingsandclaws.WingsAndClaws;

public class IcyPlowheadRenderer extends MobRenderer<IcyPlowheadEntity, IcyPlowheadModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[8];
    private final IcyPlowheadModel adult;
    private final IcyPlowheadModel child;

    public IcyPlowheadRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new IcyPlowheadModel.Adult(), 0.75f);
        adult = model;
        child = new IcyPlowheadModel.Child();
    }

    @Override
    public void render(IcyPlowheadEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        model = entityIn.isBaby() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void setupRotations(IcyPlowheadEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(entityLiving.xRot));
    }

    @Override
    public ResourceLocation getTextureLocation(IcyPlowheadEntity entity) {
        byte texture = 0;
        if (entity.isBaby()) texture |= 1;
        if (entity.getGender() == TameableDragonEntity.Gender.MALE) texture |= 2;
        if (entity.isSleeping()) texture |= 4;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s_%s%s", ((texture & 1) != 0) ? "child" : "adult", ((texture & 2) != 0) ? "male" : "female", ((texture & 4) != 0) ? "_sleep" : "");
            ResourceLocation location = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/icy_plowhead/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
