package random.wings.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.model.HatchetBeakModel;
import random.wings.entity.HatchetBeakEntity;

import javax.annotation.Nullable;

public class HatchetBeakRenderer extends MobRenderer<HatchetBeakEntity, HatchetBeakModel> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/hatchet_beak/hatchet_beak.png");

    public HatchetBeakRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new HatchetBeakModel(), 0.75f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(HatchetBeakEntity hatchetBeakEntity) {
        return TEXTURE;
    }
}
