package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
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

	public EntityRendererFailedCow(RenderManager renderManager)
	{
		super(renderManager, new ModelCow(), 0.7f);
	}

	protected ResourceLocation getEntityTexture(EntityCow entity)
	{
		return cowTextures;
	}
}
