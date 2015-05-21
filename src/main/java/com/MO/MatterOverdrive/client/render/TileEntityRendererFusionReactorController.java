package com.MO.MatterOverdrive.client.render;

import cofh.lib.gui.GuiColor;
import cofh.lib.util.helpers.BlockHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.tile.TileEntityMachineFusionReactorController;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/14/2015.
 */
public class TileEntityRendererFusionReactorController extends TileEntitySpecialRenderer
{
    public TileEntityRendererFusionReactorController(){

    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks)
    {
        TileEntityMachineFusionReactorController controller = (TileEntityMachineFusionReactorController)tileEntity;
        if (!controller.isValidStructure()) {
            int back = tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

            glPushMatrix();
            glTranslated(x, y, z);

            for (int i = 0; i < TileEntityMachineFusionReactorController.positionsCount; i++) {
                Vector3f pos = controller.getPosition(i,back);

                glPushMatrix();
                glTranslated(pos.x, pos.y, pos.z);
                //glRotated(90, 1, 0, 0);
                glEnable(GL_BLEND);
                glBlendFunc(GL_ONE, GL_ONE);
                bindTexture(TileEntityRendererPatternMonitor.screenTextureBack);
                glTranslated(0.1, 0.1, 0.1);
                RenderUtils.drawCube(0.8, 0.8, 0.8, Reference.COLOR_HOLO);
                glDisable(GL_BLEND);
                glPopMatrix();

            }
            glPopMatrix();
        }

        renderInfo(x, y, z, controller);
    }

    private void renderInfo(double x,double y,double z,TileEntityMachineFusionReactorController controller) {
        int meta = controller.getWorldObj().getBlockMetadata(controller.xCoord, controller.yCoord, controller.zCoord);
        ForgeDirection side = ForgeDirection.getOrientation(meta);

        GuiColor color = Reference.COLOR_HOLO;
        if (!controller.isValidStructure())
            color = Reference.COLOR_HOLO_RED;

        RenderUtils.beginDrawinngBlockScreen(x, y, z, side,color,controller);

        String[] info = controller.getMonitorInfo().split(" ");

        if (controller.isValidStructure())
        {
            RenderUtils.drawScreenInfo(info, color,side);
        } else {

            RenderUtils.drawScreenInfo(info,color,side);
        }

        RenderUtils.endDrawinngBlockScreen();

    }

    private FontRenderer fontRenderer()
    {
        return Minecraft.getMinecraft().fontRenderer;
    }
}
