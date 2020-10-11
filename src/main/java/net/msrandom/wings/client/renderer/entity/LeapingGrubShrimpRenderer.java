package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.LeapingGrubShrimpModel;
import net.msrandom.wings.client.renderer.entity.model.SugarscaleModel;
import net.msrandom.wings.entity.passive.LeapingGrubShrimpEntity;
import net.msrandom.wings.entity.passive.SugarscaleEntity;

public class LeapingGrubShrimpRenderer extends MobRenderer<LeapingGrubShrimpEntity, LeapingGrubShrimpModel<LeapingGrubShrimpEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/fish/leaping_grub_shrimp.png");

    public LeapingGrubShrimpRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new LeapingGrubShrimpModel<>(), 0.4F);
    }

    public ResourceLocation getEntityTexture(LeapingGrubShrimpEntity entity) {
        return TEXTURE;
    }
}