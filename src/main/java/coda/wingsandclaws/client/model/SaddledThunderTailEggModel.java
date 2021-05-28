package coda.wingsandclaws.client.renderer.entity.model;

import coda.wingsandclaws.entity.item.SaddledThunderTailEggEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SaddledThunderTailEggModel extends SegmentedModel<SaddledThunderTailEggEntity> {
    public ModelRenderer egg;

    public SaddledThunderTailEggModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.egg = new ModelRenderer(this, 0, 0);
        this.egg.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.egg.addBox(-4.0F, -12.0F, -4.0F, 8.0F, 12.0F, 8.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void setRotationAngles(SaddledThunderTailEggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(egg);
    }
}
