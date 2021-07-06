package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.model.SongvernModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.entity.SongvernEntity;

public class SongvernRenderer extends MobRenderer<SongvernEntity, SongvernModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[16];
    private final SongvernModel adult;
    private final SongvernModel child;

    public SongvernRenderer(EntityRendererManager manager) {
        super(manager, new SongvernModel.Adult(), 0.15f);
        adult = model;
        child = new SongvernModel.Child();
    }

    @Override
    public void render(SongvernEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        model = entityIn.isBaby() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(SongvernEntity entity) {
        int variant = entity.isBaby() ? 1 : entity.getVariant() + 2;
        ResourceLocation texture = TEXTURES[variant - 1];
        if (texture == null) {
            TEXTURES[variant - 1] = texture = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/songvern/songvern_" + variant + ".png");
        }
        return texture;
    }
}
