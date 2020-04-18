package random.wings.client.renderer.tileentity.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class PlowheadShieldModel extends Model {
    public RendererModel base;
    public RendererModel bottom;
    public RendererModel handle;
    public RendererModel front1;
    public RendererModel front1_1;

    public PlowheadShieldModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.front1 = new RendererModel(this, 34, 0);
        this.front1.setRotationPoint(-0.5F, -6.0F, 0.0F);
        this.front1.addBox(0.0F, -3.0F, -3.0F, 12, 6, 3, 0.0F);
        this.setRotateAngle(front1, 0.0F, -0.15707963267948966F, -0.2617993877991494F);
        this.base = new RendererModel(this, 0, 0);
        this.base.setRotationPoint(0.0F, 11.0F, -1.0F);
        this.base.addBox(-7.0F, -9.0F, -1.5F, 14, 18, 3, 0.0F);
        this.bottom = new RendererModel(this, 34, 12);
        this.bottom.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.bottom.addBox(-5.0F, 0.0F, -1.5F, 10, 4, 3, 0.0F);
        this.front1_1 = new RendererModel(this, 34, 0);
        this.front1_1.mirror = true;
        this.front1_1.setRotationPoint(0.5F, -6.0F, 0.0F);
        this.front1_1.addBox(-12.0F, -3.0F, -3.0F, 12, 6, 3, 0.0F);
        this.setRotateAngle(front1_1, 0.0F, 0.15707963267948966F, 0.2617993877991494F);
        this.handle = new RendererModel(this, 29, 19);
        this.handle.setRotationPoint(0.0F, 2.0F, 1.5F);
        this.handle.addBox(-1.0F, -3.0F, 0.0F, 2, 6, 6, 0.0F);
        this.base.addChild(this.front1);
        this.base.addChild(this.bottom);
        this.base.addChild(this.front1_1);
        this.base.addChild(this.handle);
    }

    public void render() {
        this.base.render(0.625f);
    }

    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
