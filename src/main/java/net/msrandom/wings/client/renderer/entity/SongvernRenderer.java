package net.msrandom.wings.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.SongvernModel;
import net.msrandom.wings.entity.passive.SongvernEntity;

public class SongvernRenderer extends MobRenderer<SongvernEntity, SongvernModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[16];
    private final SongvernModel adult;
    private final SongvernModel child;

    public SongvernRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new SongvernModel.Adult(), 0.15f);
        adult = entityModel;
        child = new SongvernModel.Child();
    }

    @Override
    public void render(SongvernEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityModel = entityIn.isChild() ? child : adult;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(SongvernEntity entity) {
        int variant = entity.isChild() ? 1 : entity.getVariant() + 2;
        ResourceLocation texture = TEXTURES[variant - 1];
        if (texture == null) {
            TEXTURES[variant - 1] = texture = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/songvern/songvern_" + variant + ".png");
        }
        return texture;
    }
}
