package random.wings.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.model.IcyPlowheadModel;
import random.wings.entity.IcyPlowheadEntity;

import javax.annotation.Nullable;

public class IcyPlowheadRenderer extends MobRenderer<IcyPlowheadEntity, IcyPlowheadModel> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/icy_plowhead/icy_plowhead.png");
	public static final ResourceLocation FEMALE = new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/icy_plowhead/icy_plowhead_female.png");

	public IcyPlowheadRenderer(EntityRendererManager p_i50961_1_) {
		super(p_i50961_1_, new IcyPlowheadModel(), 0.75f);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(IcyPlowheadEntity IcyPlowheadEntity) {
		return IcyPlowheadEntity.getGender() ? TEXTURE : FEMALE;
	}
}
