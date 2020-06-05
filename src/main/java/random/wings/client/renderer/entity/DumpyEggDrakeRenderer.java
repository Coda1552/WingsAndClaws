package random.wings.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.entity.layer.LayerDEDBandana;
import random.wings.client.renderer.entity.model.DumpyEggDrakeModel;
import random.wings.entity.passive.DumpyEggDrakeEntity;

public class DumpyEggDrakeRenderer extends MobRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[8];
    private final DumpyEggDrakeModel adult;
    private final DumpyEggDrakeModel child;

    public DumpyEggDrakeRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new DumpyEggDrakeModel.Adult(), 0.5f);
        adult = entityModel;
        child = new DumpyEggDrakeModel.Child();
        addLayer(new LayerDEDBandana(this));
    }

    @Override
    public void render(DumpyEggDrakeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityModel = entityIn.isChild() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void preRenderCallback(DumpyEggDrakeEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
        if (entitylivingbaseIn.isSleeping())
            matrixStackIn.translate(0, entitylivingbaseIn.isChild() ? 0.325 : 0.65, 0);
    }

    @Override
    public ResourceLocation getEntityTexture(DumpyEggDrakeEntity entity) {
        byte texture = 0;
        if (entity.isChild()) texture |= 1;
        if (entity.getGender()) texture |= 2;
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
