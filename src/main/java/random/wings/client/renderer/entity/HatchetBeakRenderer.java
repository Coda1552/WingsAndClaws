package random.wings.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.entity.model.HatchetBeakModel;
import random.wings.entity.passive.HatchetBeakEntity;

import javax.annotation.Nullable;

public class HatchetBeakRenderer extends MobRenderer<HatchetBeakEntity, HatchetBeakModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entity/hatchet_beak/hatchet_beak.png");

    public HatchetBeakRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new HatchetBeakModel(), 0.75f);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(HatchetBeakEntity hatchetBeakEntity) {
        return TEXTURE;
    }
}
