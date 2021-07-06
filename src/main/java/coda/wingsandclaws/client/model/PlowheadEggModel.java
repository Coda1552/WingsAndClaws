package coda.wingsandclaws.client.model;

import coda.wingsandclaws.entity.item.PlowheadEggEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PlowheadEggModel extends SegmentedModel<PlowheadEggEntity> {
    public ModelRenderer inner;
    public ModelRenderer outter;

    public PlowheadEggModel() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.inner = new ModelRenderer(this, 0, 0);
        this.inner.setPos(0.0F, 20.5F, 0.0F);
        this.inner.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.outter = new ModelRenderer(this, 20, 0);
        this.outter.setPos(0.0F, 0.0F, 0.0F);
        this.outter.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7, 0.0F);
        this.inner.addChild(this.outter);
    }

    @Override
    public void setupAnim(PlowheadEggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(inner);
    }
}
