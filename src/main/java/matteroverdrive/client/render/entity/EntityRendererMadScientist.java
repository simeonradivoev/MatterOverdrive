package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/30/2015.
 */
public class EntityRendererMadScientist extends RenderVillager
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTETIES + "mad_scientist.png");

    public EntityRendererMadScientist(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityVillager entity)
    {
        return texture;
    }
}
