package random.wings.client.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import random.wings.entity.IcyPlowheadEntity;

public class IcyPlowheadModel extends EntityModel<IcyPlowheadEntity> {
    public RendererModel body;
    public RendererModel armLeft;
    public RendererModel legLeft;
    public RendererModel armRight;
    public RendererModel legRight;
    public RendererModel tail1;
    public RendererModel head;
    public RendererModel tail2;
    public RendererModel tail3;
    public RendererModel headLeft;
    public RendererModel headRight;

    public IcyPlowheadModel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.tail1 = new RendererModel(this, 0, 83);
        this.tail1.setRotationPoint(0.0F, -1.0F, 18.0F);
        this.tail1.addBox(-3.0F, -5.0F, 0.0F, 6, 10, 22, 0.0F);
        this.legRight = new RendererModel(this, 50, 0);
        this.legRight.mirror = true;
        this.legRight.setRotationPoint(-7.0F, 6.0F, 12.0F);
        this.legRight.addBox(-10.0F, -1.0F, -3.0F, 10, 2, 6, 0.0F);
        this.body = new RendererModel(this, 25, 31);
        this.body.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.body.addBox(-7.0F, -7.0F, -18.0F, 14, 14, 36, 0.0F);
        this.tail3 = new RendererModel(this, 48, -9);
        this.tail3.setRotationPoint(0.0F, 0.0F, 22.0F);
        this.tail3.addBox(0.0F, -6.0F, 0.0F, 0, 12, 22, 0.0F);
        this.legLeft = new RendererModel(this, 50, 0);
        this.legLeft.setRotationPoint(7.0F, 6.0F, 12.0F);
        this.legLeft.addBox(0.0F, -1.0F, -3.0F, 10, 2, 6, 0.0F);
        this.tail2 = new RendererModel(this, 58, 87);
        this.tail2.setRotationPoint(0.0F, -1.0F, 22.0F);
        this.tail2.addBox(-2.0F, -3.0F, 0.0F, 4, 6, 22, 0.0F);
        this.armRight = new RendererModel(this, 0, 53);
        this.armRight.mirror = true;
        this.armRight.setRotationPoint(-7.0F, 6.0F, -9.0F);
        this.armRight.addBox(-12.0F, -1.0F, -4.0F, 12, 2, 8, 0.0F);
        this.headLeft = new RendererModel(this, 0, 24);
        this.headLeft.mirror = true;
        this.headLeft.setRotationPoint(5.0F, -1.5F, -6.0F);
        this.headLeft.addBox(-2.0F, -6.0F, -10.0F, 4, 8, 20, 0.0F);
        this.setRotateAngle(headLeft, 0.2617993877991494F, 0.3490658503988659F, 0.0F);
        this.headRight = new RendererModel(this, 0, 24);
        this.headRight.setRotationPoint(-5.0F, -1.5F, -6.0F);
        this.headRight.addBox(-2.0F, -6.0F, -10.0F, 4, 8, 20, 0.0F);
        this.setRotateAngle(headRight, 0.2617993877991494F, -0.3490658503988659F, 0.0F);
        this.head = new RendererModel(this, 0, 0);
        this.head.setRotationPoint(0.0F, 2.0F, -18.0F);
        this.head.addBox(-4.0F, -5.0F, -14.0F, 8, 10, 14, 0.0F);
        this.armLeft = new RendererModel(this, 0, 53);
        this.armLeft.setRotationPoint(7.0F, 6.0F, -9.0F);
        this.armLeft.addBox(0.0F, -1.0F, -4.0F, 12, 2, 8, 0.0F);
        this.body.addChild(this.tail1);
        this.body.addChild(this.legRight);
        this.tail2.addChild(this.tail3);
        this.body.addChild(this.legLeft);
        this.tail1.addChild(this.tail2);
        this.body.addChild(this.armRight);
        this.head.addChild(this.headLeft);
        this.head.addChild(this.headRight);
        this.body.addChild(this.head);
        this.body.addChild(this.armLeft);
    }

    @Override
    public void render(IcyPlowheadEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.body.render(f5);
    }

    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
