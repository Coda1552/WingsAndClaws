package random.wings.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.model.DumpyEggDrakeModel;
import random.wings.entity.DumpyEggDrakeEntity;

import javax.annotation.Nullable;

public class DumpyEggDrakeRenderer extends MobRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[8];
    private DumpyEggDrakeModel adult;
    private DumpyEggDrakeModel child;

    public DumpyEggDrakeRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new DumpyEggDrakeModel.Adult(), 0.5f);
        adult = entityModel;
        child = new DumpyEggDrakeModel.Child();
        addLayer(new LayerDEDBandana(this));
    }

    @Override
    public void doRender(DumpyEggDrakeEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        entityModel = entity.isChild() ? child : adult;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(DumpyEggDrakeEntity entity, float partialTickTime) {
        super.preRenderCallback(entity, partialTickTime);
        if (entity.isSleeping())
            GlStateManager.translated(0, entity.isChild() ? 0.325 : 0.65, 0);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(DumpyEggDrakeEntity entity) {
        byte texture = 0;
        if (entity.isChild()) texture |= 1;
        if (entity.getGender()) texture |= 2;
        if (entity.isSleeping()) texture |= 4;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s_%s%s", ((texture & 1) != 0) ? "child" : "adult", ((texture & 2) != 0) ? "male" : "female", ((texture & 4) != 0) ? "_sleep" : "");
            ResourceLocation location = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/dumpy_egg_drake/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
