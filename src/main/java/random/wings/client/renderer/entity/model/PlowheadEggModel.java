package random.wings.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import random.wings.entity.item.PlowheadEggEntity;

public class PlowheadEggModel extends EntityModel<PlowheadEggEntity> {
    public RendererModel inner;
    public RendererModel outter;

    public PlowheadEggModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.inner = new RendererModel(this, 0, 0);
        this.inner.setRotationPoint(0.0F, 20.5F, 0.0F);
        this.inner.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.outter = new RendererModel(this, 20, 0);
        this.outter.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.outter.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7, 0.0F);
        this.inner.addChild(this.outter);
    }

    @Override
    public void render(PlowheadEggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.inner.render(scale);
    }
}
