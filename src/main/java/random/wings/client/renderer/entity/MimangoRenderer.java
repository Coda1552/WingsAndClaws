package random.wings.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.entity.model.MimangoModel;
import random.wings.entity.passive.MimangoEntity;

import javax.annotation.Nullable;

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
    public void doRender(MimangoEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        entityModel = entity.isChild() ? child : adult;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(MimangoEntity entity) {
        int variant = entity.isChild() ? 1 : entity.getVariant() + 2;
        ResourceLocation texture = TEXTURES[variant];
        if (texture == null) {
            TEXTURES[variant] = texture = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/mimango/texture_" + variant);
        }
        return texture;
    }
}
