package com.MO.MatterOverdrive.client.render;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.TileEntitiyMachinePatternMonitor;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
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

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks)
    {
        glPushMatrix();

        ForgeDirection direction = ForgeDirection.getOrientation(tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord,tileEntity.yCoord,tileEntity.zCoord));
        glTranslated(x, y, z);

        if (direction == ForgeDirection.EAST)
        {
            glTranslated(0.4,1,1);
            glRotatef(90, 1, 0, 0);
            glRotatef(-90, 0, 0, 1);
        }
        else if (direction == ForgeDirection.WEST)
        {
            glTranslated(0.6,1,0);
            glRotatef(90, 1, 0, 0);
            glRotatef(90, 0, 0, 1);
        }
        else if (direction == ForgeDirection.SOUTH)
        {
            glTranslated(0,1,0.4);
            glRotatef(90, 1, 0, 0);
        }
        else if (direction == ForgeDirection.NORTH)
        {
            glTranslated(1,1,0.6);
            glRotatef(90, 1, 0, 0);
            glRotatef(180, 0, 0, 1);
        }




        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);
        RenderUtils.disableLightmap();
        Minecraft.getMinecraft().renderEngine.bindTexture(screenTextureBack);
        glColor3f(Reference.COLOR_HOLO.getFloatR(), Reference.COLOR_HOLO.getFloatG(), Reference.COLOR_HOLO.getFloatB());

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.addVertexWithUV(0, 0, 1, 0, 1);
        tessellator.addVertexWithUV(1, 0, 1, 1, 1);
        tessellator.addVertexWithUV(1, 0, 0, 1, 0);
        tessellator.draw();

        glTranslated(0, 0.05, 0);

        if (tileEntity instanceof TileEntitiyMachinePatternMonitor)
        {
            TileEntitiyMachinePatternMonitor monitor = (TileEntitiyMachinePatternMonitor) tileEntity;
            glPushMatrix();
            glTranslated(0.45, 0, 0.33);
            glScaled(0.03, 0.03, 0.03);
            glRotatef(90, 1, 0, 0);
            Minecraft.getMinecraft().fontRenderer.drawString(Integer.toString(monitor.getDatabases().size()), 0, 0, 0x78a1b3);
            glPopMatrix();
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(screenTexture);
        glColor3f(Reference.COLOR_HOLO.getFloatR() * 0.7f, Reference.COLOR_HOLO.getFloatG() * 0.7f, Reference.COLOR_HOLO.getFloatB() * 0.7f);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.addVertexWithUV(0, 0, 1, 0, 1);
        tessellator.addVertexWithUV(1,0,1,1,1);
        tessellator.addVertexWithUV(1, 0, 0, 1, 0);
        tessellator.draw();

        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }
}
