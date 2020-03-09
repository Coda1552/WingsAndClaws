package random.wings.client.renderer;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.model.DumpyEggDrakeModel;
import random.wings.entity.DumpyEggDrakeEntity;

import java.util.HashMap;
import java.util.Map;

public class LayerDEDBandana extends LayerRenderer<DumpyEggDrakeEntity, DumpyEggDrakeModel> {
    private static final Map<Integer, ResourceLocation> TEXTURES = new HashMap<>();

    public LayerDEDBandana(DumpyEggDrakeRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(DumpyEggDrakeEntity entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
        if (entityIn.isTamed() && !entityIn.isInvisible()) {
            DyeColor color = entityIn.getBandanaColor();
            this.bindTexture(TEXTURES.computeIfAbsent((color.getId() << 1) | (entityIn.isChild() ? 1 : 0), k -> new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/dumpy_egg_drake/bandana/" + ((k & 1) > 0 ? "baby_" : "") + "bandana_" + DyeColor.byId(k >> 1) + ".png")));
            this.getEntityModel().render(entityIn, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
