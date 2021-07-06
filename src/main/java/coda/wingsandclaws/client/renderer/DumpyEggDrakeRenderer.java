package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.renderer.layer.DEDBandanaLayer;
import coda.wingsandclaws.client.model.DumpyEggDrakeModel;
import coda.wingsandclaws.entity.util.TameableDragonEntity;
import coda.wingsandclaws.entity.DumpyEggDrakeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import coda.wingsandclaws.WingsAndClaws;

public class DumpyEggDrakeRenderer extends MobRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[8];
    private final DumpyEggDrakeModel adult;
    private final DumpyEggDrakeModel child;

    public DumpyEggDrakeRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new DumpyEggDrakeModel.Adult(), 0.5f);
        adult = model;
        child = new DumpyEggDrakeModel.Child();
        addLayer(new DEDBandanaLayer(this));
    }

    @Override
    public void render(DumpyEggDrakeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        model = entityIn.isBaby() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void scale(DumpyEggDrakeEntity entity, MatrixStack matrixStackIn, float partialTickTime) {
        super.scale(entity, matrixStackIn, partialTickTime);
        matrixStackIn.translate(0, (entity.isBaby() ? 0.325 : 0.65) * entity.sleepTimer.get(partialTickTime), 0);
    }

    @Override
    public ResourceLocation getTextureLocation(DumpyEggDrakeEntity entity) {
        byte texture = 0;
        if (entity.isBaby()) texture |= 1;
        if (entity.getGender() == TameableDragonEntity.Gender.MALE) texture |= 2;
        if (entity.isSleeping()) texture |= 4;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s_%s%s", ((texture & 1) != 0) ? "child" : "adult", ((texture & 2) != 0) ? "male" : "female", ((texture & 4) != 0) ? "_sleep" : "");
            ResourceLocation location = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/dumpy_egg_drake/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
