package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.SugarscaleModel;
import net.msrandom.wings.entity.passive.SugarscaleEntity;

public class SugarscaleRenderer extends MobRenderer<SugarscaleEntity, SugarscaleModel<SugarscaleEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/fish/sugarscale.png");

    public SugarscaleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SugarscaleModel<>(), 0.3F);
    }

    public ResourceLocation getEntityTexture(SugarscaleEntity entity) {
        return TEXTURE;
    }
}