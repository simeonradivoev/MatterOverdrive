package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/29/2015.
 */
public class EntityRendererFailedSheep extends RenderSheep
{

	private static final ResourceLocation shearedSheepTextures = new ResourceLocation(Reference.PATH_ENTETIES + "failed_sheep.png");

	public EntityRendererFailedSheep(RenderManager renderManager, ModelBase model1, float f)
	{
		super(renderManager, model1, f);
	}

	protected ResourceLocation getEntityTexture(EntitySheep entity)
	{
		return shearedSheepTextures;
	}
}
