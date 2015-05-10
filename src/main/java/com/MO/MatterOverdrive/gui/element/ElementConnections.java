package com.MO.MatterOverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.tile.TileEntitiyMachinePacketQueue;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 5/10/2015.
 */
public class ElementConnections extends MOElementBase
{

    TileEntitiyMachinePacketQueue machine;

    public ElementConnections(GuiBase gui, int posX, int posY, int width, int height,TileEntitiyMachinePacketQueue machine)
    {
        super(gui, posX, posY, width, height);
        this.machine = machine;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        for (int i = 0;i < 6;i++)
        {
            BlockPosition connection = machine.getConnection(i);
            GL11.glColor3f(1, 1, 1);
            int x = 50;
            int y = 42 + i * 19;

            if (connection != null)
            {
                String info = ForgeDirection.getOrientation(i).name() + " : " + connection.getTileEntity(machine.getWorldObj()).getBlockType().getLocalizedName();

                MOElementButton.NORMAL_TEXTURE.Render(x - 6,y - 6,sizeX + 12,19);
                getFontRenderer().drawString(info,x,y,0xFFFFFF);
            }
            else
            {
                MOElementButton.HOVER_TEXTURE_DARK.Render(x - 6,y - 6,80,19);
                getFontRenderer().drawString(ForgeDirection.getOrientation(i).name() + " : None", x, y, 0xFFFFFF);
            }
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
