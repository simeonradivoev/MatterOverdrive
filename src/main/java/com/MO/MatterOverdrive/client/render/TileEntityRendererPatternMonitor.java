package com.MO.MatterOverdrive.client.render;

import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternMonitor;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 4/29/2015.
 */
public class TileEntityRendererPatternMonitor extends TileEntitySpecialRenderer
{
    public static ResourceLocation screenTexture = new ResourceLocation(Reference.PATH_BLOCKS + "pattern_monitor_holo.png");
    public static ResourceLocation screenTextureBack = new ResourceLocation(Reference.PATH_BLOCKS + "pattern_monitor_holo_back.png");
    public static ResourceLocation screenTextureGlow = new ResourceLocation(Reference.PATH_FX + "holo_monitor_glow.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks) {
        glPushMatrix();

        int meta = tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        ForgeDirection direction = ForgeDirection.getOrientation(meta);

        /*float thickness = 5f * (1f / 16f);

        if (direction == ForgeDirection.EAST) {
            glTranslated(thickness, 1, 1);
            glRotatef(90, 1, 0, 0);
            glRotatef(-90, 0, 0, 1);
        } else if (direction == ForgeDirection.WEST) {
            glTranslated(1 - thickness, 1, 0);
            glRotatef(90, 1, 0, 0);
            glRotatef(90, 0, 0, 1);
        } else if (direction == ForgeDirection.SOUTH) {
            glTranslated(0, 1, thickness);
            glRotatef(90, 1, 0, 0);
        } else if (direction == ForgeDirection.NORTH) {
            glTranslated(1, 1, 1 - thickness);
            glRotatef(90, 1, 0, 0);
            glRotatef(180, 0, 0, 1);
        }*/

        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);
        RenderUtils.disableLightmap();

        RenderUtils.beginDrawinngBlockScreen(x, y, z, direction, Reference.COLOR_HOLO, tileEntity, -0.65);
        glTranslated(0, 0, -0.05);
        Minecraft.getMinecraft().renderEngine.bindTexture(screenTexture);
        glColor3f(Reference.COLOR_HOLO.getFloatR() * 0.7f, Reference.COLOR_HOLO.getFloatG() * 0.7f, Reference.COLOR_HOLO.getFloatB() * 0.7f);

        RenderUtils.drawPlane(1);

        if (tileEntity instanceof TileEntityMachinePatternMonitor) {
            TileEntityMachinePatternMonitor monitor = (TileEntityMachinePatternMonitor) tileEntity;
            glPushMatrix();
            glTranslated(0.47, 0.33, 0);
            glScaled(0.03, 0.03, 0.03);
            Minecraft.getMinecraft().fontRenderer.drawString(Integer.toString(monitor.getDatabases().size()), 0, 0, 0x78a1b3);
            glPopMatrix();
        }

        RenderUtils.endDrawinngBlockScreen();

        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }
}
