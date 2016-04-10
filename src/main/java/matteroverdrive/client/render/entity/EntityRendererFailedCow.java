package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityRendererFailedCow extends RenderCow
{

	private static final ResourceLocation cowTextures = new ResourceLocation(Reference.PATH_ENTETIES + "failed_cow.png");

	public EntityRendererFailedCow(RenderManager renderManager, ModelBase model, float f)
	{
		super(renderManager, model, f);
	}

	protected ResourceLocation getEntityTexture(EntityCow entity)
	{
		return cowTextures;
	}
}
