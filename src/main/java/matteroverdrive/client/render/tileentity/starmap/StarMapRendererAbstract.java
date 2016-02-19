package matteroverdrive.client.render.tileentity.starmap;

import matteroverdrive.Reference;
import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.fx.MOEntityFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.Sphere;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public abstract class StarMapRendererAbstract implements ISpaceBodyHoloRenderer
{
    protected MOEntityFX.ParticleIcon star_icon = MOEntityFX.ParticleIcon.fromWithAndHeight(0,0,32,32,128);
    protected MOEntityFX.ParticleIcon selectedIcon = MOEntityFX.ParticleIcon.fromWithAndHeight(32,0,32,32,128);
    protected MOEntityFX.ParticleIcon currentIcon = MOEntityFX.ParticleIcon.fromWithAndHeight(64,0,32,32,128);
    protected final Sphere sphere;
    protected final Disk disk;
    protected final Random random;
    protected final FontRenderer fontRenderer;

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
