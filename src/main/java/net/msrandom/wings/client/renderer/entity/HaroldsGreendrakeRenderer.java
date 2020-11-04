package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.HaroldsGreendrakeModel;
import net.msrandom.wings.entity.passive.HaroldsGreendrakeEntity;

public class HaroldsGreendrakeRenderer extends MobRenderer<HaroldsGreendrakeEntity, HaroldsGreendrakeModel<HaroldsGreendrakeEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/harolds_greendrake.png");

    public HaroldsGreendrakeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HaroldsGreendrakeModel<>(), 0.7F);
    }

    public ResourceLocation getEntityTexture(HaroldsGreendrakeEntity entity) {
        return TEXTURE;
    }
}