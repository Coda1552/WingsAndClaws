package random.wings.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.entity.model.PlowheadEggModel;
import random.wings.client.renderer.tileentity.PlowheadItemEggRenderer;
import random.wings.entity.item.PlowheadEggEntity;

import javax.annotation.Nullable;

public class PlowheadEggRenderer extends EntityRenderer<PlowheadEggEntity> implements IEntityRenderer<PlowheadEggEntity, PlowheadEggModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/icy_plowhead/egg.png");
    private static PlowheadItemEggRenderer itemRenderer;
    private final PlowheadEggModel model = new PlowheadEggModel();

    public PlowheadEggRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        itemRenderer = new PlowheadItemEggRenderer(this);
    }

    public static PlowheadItemEggRenderer getItemRenderer() {
        return itemRenderer;
    }

    @Override
    public void doRender(PlowheadEggEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.translated(x, y, z);
        model.render(this::bindCurrentTexture);
    }

    public void bindCurrentTexture() {
        bindTexture(getTexture());
    }

    public ResourceLocation getTexture() {
        return TEXTURE;
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
