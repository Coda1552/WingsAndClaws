package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.model.MimangoModel;
import coda.wingsandclaws.entity.MimangoEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import coda.wingsandclaws.WingsAndClaws;

public class MimangoRenderer extends MobRenderer<MimangoEntity, MimangoModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[6];
    private final MimangoModel adult;
    private final MimangoModel child;

    public MimangoRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new MimangoModel.Adult(), 0.1f);
        adult = entityModel;
        child = new MimangoModel.Baby();
    }

    @Override
    public void render(MimangoEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityModel = entityIn.isChild() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(MimangoEntity entity) {
        int variant = entity.isChild() ? 1 : entity.getVariant() + 2;
        ResourceLocation texture = TEXTURES[variant - 1];
        if (texture == null) {
            TEXTURES[variant - 1] = texture = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/mimango/mimango_" + variant + ".png");
        }
        return texture;
    }
}
