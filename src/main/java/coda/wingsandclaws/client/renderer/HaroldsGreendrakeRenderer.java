package coda.wingsandclaws.client.renderer;

import coda.wingsandclaws.client.renderer.layer.HaroldsGreendrakeItemLayer;
import coda.wingsandclaws.client.model.HaroldsGreendrakeModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.entity.HaroldsGreendrakeEntity;

public class HaroldsGreendrakeRenderer extends MobRenderer<HaroldsGreendrakeEntity, HaroldsGreendrakeModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/harolds_greendrake.png");

    public HaroldsGreendrakeRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HaroldsGreendrakeModel(), 0.7F);
        addLayer(new HaroldsGreendrakeItemLayer(this));
    }

    public ResourceLocation getEntityTexture(HaroldsGreendrakeEntity entity) {
        return TEXTURE;
    }
}