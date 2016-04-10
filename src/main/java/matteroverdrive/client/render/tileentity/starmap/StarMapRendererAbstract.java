package matteroverdrive.client.render.tileentity.starmap;

import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.Sphere;

import java.util.Random;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public abstract class StarMapRendererAbstract implements ISpaceBodyHoloRenderer
{
	protected final Sphere sphere;
	protected final Disk disk;
	protected final Random random;
	protected final FontRenderer fontRenderer;
	protected TextureAtlasSprite star_icon = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(RenderParticlesHandler.star);
	protected TextureAtlasSprite selectedIcon = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(RenderParticlesHandler.selection);
	protected TextureAtlasSprite currentIcon = ClientProxy.renderHandler.getRenderParticlesHandler().getSprite(RenderParticlesHandler.marker);

	public StarMapRendererAbstract()
	{
		sphere = new Sphere();
		disk = new Disk();
		random = new Random();
		/*try
        {
            sphere_model = OBJLoader.instance.loadModel(new ResourceLocation(Reference.MODEL_SPHERE));
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
		fontRenderer = Minecraft.getMinecraft().fontRendererObj;
	}
}
