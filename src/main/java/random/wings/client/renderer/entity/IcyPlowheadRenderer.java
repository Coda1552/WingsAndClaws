package random.wings.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.entity.model.IcyPlowheadModel;
import random.wings.entity.monster.IcyPlowheadEntity;

public class IcyPlowheadRenderer extends MobRenderer<IcyPlowheadEntity, IcyPlowheadModel> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[4];

    public IcyPlowheadRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new IcyPlowheadModel(), 0.75f);
    }

    @Override
    public ResourceLocation getEntityTexture(IcyPlowheadEntity entity) {
        byte texture = 0;
        if (entity.getGender()) texture |= 1;
        if (entity.isSleeping()) texture |= 2;
        if (TEXTURES[texture] == null) {
            String entityTexture = String.format("%s%s", ((texture & 1) != 0) ? "male" : "female", ((texture & 2) != 0) ? "_sleep" : "");
            ResourceLocation location = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/icy_plowhead/" + entityTexture + ".png");
            TEXTURES[texture] = location;
            return location;
        }
        return TEXTURES[texture];
    }
}
