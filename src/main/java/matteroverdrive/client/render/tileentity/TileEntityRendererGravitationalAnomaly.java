package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/12/2015.
 */
public class TileEntityRendererGravitationalAnomaly extends TileEntitySpecialRenderer
{
    public static final ResourceLocation core = new ResourceLocation(Reference.PATH_BLOCKS + "gravitational_anomaly_core.png");
    public static final ResourceLocation glow = new ResourceLocation(Reference.PATH_BLOCKS + "gravitational_anomaly_glow.png");
    public static final ResourceLocation black = new ResourceLocation(Reference.PATH_BLOCKS + "black.png");

    private IModelCustom sphere_model;

    public TileEntityRendererGravitationalAnomaly()
    {
        sphere_model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MODEL_SPHERE));
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        long time = Minecraft.getMinecraft().theWorld.getWorldTime();
        float speed = ((TileEntityGravitationalAnomaly)tileEntity).getBreakStrength();
        double resonateSpeed = 0.2;
        double radius = ((TileEntityGravitationalAnomaly)tileEntity).getEventHorizon();
        if (((TileEntityGravitationalAnomaly) tileEntity).consumedCount > 0) {
            radius += radius * 0.02 * ((TileEntityGravitationalAnomaly) tileEntity).consumedCount;
            ((TileEntityGravitationalAnomaly) tileEntity).consumedCount -= 1;
        }

        radius = radius * Math.sin(time * resonateSpeed) * 0.1 + radius * 0.9;

        glPushMatrix();
        glDisable(GL_LIGHTING);
        //RenderUtils.disableLightmap();

        glTranslated(x + 0.5, y + 0.5, z + 0.5);
        glScaled(radius, radius, radius);

        glDisable(GL_CULL_FACE);
        bindTexture(black);
        glColor4d(0, 0, 0, 1);
        sphere_model.renderAll();
        glEnable(GL_CULL_FACE);

        glEnable(GL_BLEND);
        glScaled(2, 2, 2);
        glRotated(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * ticks , 0, -1, 0);
        glRotated(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * ticks, 1, 0, 0);
        glRotated(time * speed, 0, 0, 1);
        glTranslated(-0.5, -0.5, 0);
        glColor3d(1, 1, 1);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        bindTexture(core);
        RenderUtils.drawPlane(1);
        //glBlendFunc(GL_ONE, GL_ONE);
        bindTexture(glow);
        RenderUtils.drawPlane(1);

        glDisable(GL_BLEND);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }
}
