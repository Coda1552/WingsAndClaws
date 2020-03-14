package random.wings.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.client.renderer.entity.model.PlowheadEggModel;
import random.wings.client.renderer.tileentity.PlowheadItemEggRenderer;
import random.wings.entity.item.PlowheadEggEntity;

import javax.annotation.Nullable;

public class PlowheadEggRenderer extends EntityRenderer<PlowheadEggEntity> implements IEntityRenderer<PlowheadEggEntity, PlowheadEggModel> {
    private static PlowheadItemEggRenderer itemRenderer;
    private final PlowheadEggModel model = new PlowheadEggModel();

    protected PlowheadEggRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        itemRenderer = new PlowheadItemEggRenderer(this);
    }

    public static PlowheadItemEggRenderer getItemRenderer() {
        return itemRenderer;
    }

    @Override
    public void doRender(PlowheadEggEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        model.render(this::bindCurrentTexture);
    }

    public void bindCurrentTexture() {
        bindTexture(getTexture());
    }

    public ResourceLocation getTexture() {
        //TODO
        return null;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(PlowheadEggEntity entity) {
        return getTexture();
    }

    @Override
    public PlowheadEggModel getEntityModel() {
        return model;
    }
}
