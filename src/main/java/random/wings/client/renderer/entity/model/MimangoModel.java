package random.wings.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import random.wings.entity.passive.MimangoEntity;

public abstract class MimangoModel extends EntityModel<MimangoEntity> {
    public RendererModel body;
    public RendererModel head;
    public RendererModel tail;
    public RendererModel wingLeft;
    public RendererModel wingRight;
    public RendererModel snout;
    public RendererModel hair1;
    public RendererModel hair2;
    public RendererModel hair3;

    @Override
    public void render(MimangoEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.body.render(f5);
    }

    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public static class Adult extends MimangoModel {
        public Adult() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            this.hair1 = new RendererModel(this, 0, 0);
            this.hair1.setRotationPoint(0.0F, -2.0F, -1.0F);
            this.hair1.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair1, 0.3490658503988659F, 0.0F, 0.0F);
            this.wingLeft = new RendererModel(this, 9, 0);
            this.wingLeft.setRotationPoint(1.5F, -1.5F, 0.0F);
            this.wingLeft.addBox(0.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair2 = new RendererModel(this, 0, 0);
            this.hair2.setRotationPoint(-0.5F, -2.0F, -1.0F);
            this.hair2.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair2, 0.3490658503988659F, -0.08726646259971647F, 0.0F);
            this.snout = new RendererModel(this, 0, 14);
            this.snout.setRotationPoint(0.0F, -0.5F, -3.0F);
            this.snout.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F);
            this.wingRight = new RendererModel(this, 9, 0);
            this.wingRight.mirror = true;
            this.wingRight.setRotationPoint(-1.5F, -1.5F, 0.0F);
            this.wingRight.addBox(-4.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair3 = new RendererModel(this, 0, 0);
            this.hair3.setRotationPoint(0.5F, -2.0F, -1.0F);
            this.hair3.addBox(0.0F, 0.0F, 0.0F, 0, 1, 2, 0.0F);
            this.setRotateAngle(hair3, 0.3490658503988659F, 0.08726646259971647F, 0.0F);
            this.head = new RendererModel(this, 0, 8);
            this.head.setRotationPoint(0.0F, -0.5F, -1.0F);
            this.head.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 3, 0.0F);
            this.tail = new RendererModel(this, 0, 12);
            this.tail.setRotationPoint(0.0F, 0.0F, 2.0F);
            this.tail.addBox(0.0F, -1.5F, 0.0F, 0, 3, 5, 0.0F);
            this.body = new RendererModel(this, 0, 0);
            this.body.setRotationPoint(0.0F, 22.5F, 0.0F);
            this.body.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 4, 0.0F);
            this.head.addChild(this.hair1);
            this.body.addChild(this.wingLeft);
            this.head.addChild(this.hair2);
            this.head.addChild(this.snout);
            this.body.addChild(this.wingRight);
            this.head.addChild(this.hair3);
            this.body.addChild(this.head);
            this.body.addChild(this.tail);
        }
    }

    public static class Baby extends MimangoModel {
        public Baby() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            this.body = new RendererModel(this, 0, 0);
            this.body.setRotationPoint(0.0F, 23.0F, 0.0F);
            this.body.addBox(-1.0F, -1.0F, -1.5F, 2, 2, 3, 0.0F);
            this.wingRight = new RendererModel(this, 9, 0);
            this.wingRight.mirror = true;
            this.wingRight.setRotationPoint(-1.0F, -1.0F, -0.5F);
            this.wingRight.addBox(-4.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.snout = new RendererModel(this, 0, 14);
            this.snout.setRotationPoint(0.0F, -0.5F, -2.0F);
            this.snout.addBox(-0.5F, -0.5F, -1.0F, 1, 1, 1, 0.0F);
            this.head = new RendererModel(this, 0, 8);
            this.head.setRotationPoint(0.0F, 0.0F, -0.5F);
            this.head.addBox(-0.5F, -2.0F, -2.0F, 1, 2, 2, 0.0F);
            this.hair3 = new RendererModel(this, 0, 0);
            this.hair3.setRotationPoint(0.4F, -2.0F, -0.4F);
            this.hair3.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair3, 0.3490658503988659F, 0.08726646259971647F, 0.0F);
            this.hair1 = new RendererModel(this, 0, 0);
            this.hair1.setRotationPoint(0.0F, -2.0F, -0.4F);
            this.hair1.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair1, 0.3490658503988659F, 0.0F, 0.0F);
            this.tail = new RendererModel(this, 0, 12);
            this.tail.setRotationPoint(0.0F, 0.0F, 1.5F);
            this.tail.addBox(0.0F, -1.0F, 0.0F, 0, 2, 5, 0.0F);
            this.wingLeft = new RendererModel(this, 9, 0);
            this.wingLeft.setRotationPoint(1.0F, -1.0F, -0.5F);
            this.wingLeft.addBox(0.0F, 0.0F, -1.0F, 4, 0, 2, 0.0F);
            this.hair2 = new RendererModel(this, 0, 0);
            this.hair2.setRotationPoint(-0.4F, -2.0F, -0.4F);
            this.hair2.addBox(0.0F, 0.0F, 0.0F, 0, 1, 1, 0.0F);
            this.setRotateAngle(hair2, 0.3490658503988659F, -0.08726646259971647F, 0.0F);
            this.body.addChild(this.wingRight);
            this.head.addChild(this.snout);
            this.body.addChild(this.head);
            this.head.addChild(this.hair3);
            this.head.addChild(this.hair1);
            this.body.addChild(this.tail);
            this.body.addChild(this.wingLeft);
            this.head.addChild(this.hair2);
        }
    }
}
