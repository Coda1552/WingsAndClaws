package net.msrandom.wings.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.msrandom.wings.entity.item.PlowheadEggEntity;

public class PlowheadEggModel extends CompositeEntityModel<PlowheadEggEntity> {
    public ModelPart inner;
    public ModelPart outter;

    public PlowheadEggModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.inner = new ModelPart(this, 0, 0);
        this.inner.setPivot(0.0F, 20.5F, 0.0F);
        this.inner.addCuboid(-2.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.outter = new ModelPart(this, 20, 0);
        this.outter.setPivot(0.0F, 0.0F, 0.0F);
        this.outter.addCuboid(-3.5F, -3.5F, -3.5F, 7, 7, 7, 0.0F);
        this.inner.addChild(this.outter);
    }

    @Override
    public void setAngles(PlowheadEggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(inner);
    }
}
