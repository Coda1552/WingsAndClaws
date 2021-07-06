package coda.wingsandclaws.client.model;

import coda.wingsandclaws.entity.item.SaddledThunderTailEggEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SaddledThunderTailEggModel extends SegmentedModel<SaddledThunderTailEggEntity> {
    public ModelRenderer egg;

    public SaddledThunderTailEggModel() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.egg = new ModelRenderer(this, 0, 0);
        this.egg.setPos(0.0F, 24.0F, 0.0F);
        this.egg.addBox(-4.0F, -12.0F, -4.0F, 8.0F, 12.0F, 8.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void setupAnim(SaddledThunderTailEggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(egg);
    }
}
