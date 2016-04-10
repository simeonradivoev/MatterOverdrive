package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityRendererFailedChicken extends RenderChicken
{
	private static final ResourceLocation chickenTextures = new ResourceLocation(Reference.PATH_ENTETIES + "failed_chicken.png");

	public EntityRendererFailedChicken(RenderManager renderManager, ModelBase model, float f)
	{
		super(renderManager, model, f);
	}

	protected ResourceLocation getEntityTexture(EntityChicken entity)
	{
		return chickenTextures;
	}
}
