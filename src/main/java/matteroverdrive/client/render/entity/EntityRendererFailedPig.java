package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityRendererFailedPig extends RenderPig
{
	public static final ResourceLocation pig_texture = new ResourceLocation(Reference.PATH_ENTITIES + "failed_pig.png");

	public EntityRendererFailedPig(RenderManager renderManager)
	{
		super(renderManager, new ModelPig(), 0.7f);
	}

	protected ResourceLocation getEntityTexture(EntityPig entity)
	{
		return pig_texture;
	}
}
