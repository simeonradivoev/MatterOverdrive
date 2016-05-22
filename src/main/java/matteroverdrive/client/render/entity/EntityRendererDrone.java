package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import matteroverdrive.client.model.ModelDrone;
import matteroverdrive.entity.EntityDrone;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/22/2016.
 */
@SideOnly(Side.CLIENT)
public class EntityRendererDrone extends RenderLiving<EntityDrone>
{
	private final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTETIES + "drone_default.png");

	public EntityRendererDrone(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelDrone(), 0.5f);
	}

	@Override
	public void doRender(EntityDrone entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		mainModel = new ModelDrone();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDrone entity)
	{
		return texture;
	}
}
