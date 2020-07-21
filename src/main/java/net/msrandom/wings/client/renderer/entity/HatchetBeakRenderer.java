package net.msrandom.wings.client.renderer.entity;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.msrandom.wings.WingsAndClaws;
import net.msrandom.wings.client.renderer.entity.model.HatchetBeakModel;
import net.msrandom.wings.entity.passive.HatchetBeakEntity;

public class HatchetBeakRenderer extends MobEntityRenderer<HatchetBeakEntity, HatchetBeakModel> {
    public static final Identifier TEXTURE = new Identifier(WingsAndClaws.MOD_ID, "textures/entity/hatchet_beak/hatchet_beak.png");

    public HatchetBeakRenderer(EntityRenderDispatcher p_i50961_1_) {
        super(p_i50961_1_, new HatchetBeakModel(), 0.75f);
    }

    @Override
    public Identifier getTexture(HatchetBeakEntity hatchetBeakEntity) {
        return TEXTURE;
    }
}
