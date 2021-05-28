package coda.wingsandclaws.client.renderer.entity;

import coda.wingsandclaws.client.renderer.entity.model.SugarscaleModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.entity.passive.SugarscaleEntity;

public class SugarscaleRenderer extends MobRenderer<SugarscaleEntity, SugarscaleModel<SugarscaleEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/fish/sugarscale.png");

    public SugarscaleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SugarscaleModel<>(), 0.3F);
    }

    public ResourceLocation getEntityTexture(SugarscaleEntity entity) {
        return TEXTURE;
    }
}