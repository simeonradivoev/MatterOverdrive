/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.gui.element;

import cofh.lib.gui.GuiBase;
import cofh.lib.util.position.BlockPosition;
import matteroverdrive.tile.TileEntityMachinePacketQueue;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 5/10/2015.
 */
public class ElementConnections extends MOElementBase
{

    TileEntityMachinePacketQueue machine;

    public ElementConnections(GuiBase gui, int posX, int posY, int width, int height,TileEntityMachinePacketQueue machine)
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

                MOElementButton.NORMAL_TEXTURE.render(x - 6,y - 6,sizeX + 12,19);
                getFontRenderer().drawString(info,x,y,0xFFFFFF);
            }
            else
            {
                MOElementButton.HOVER_TEXTURE_DARK.render(x - 6,y - 6,80,19);
                getFontRenderer().drawString(ForgeDirection.getOrientation(i).name() + " : None", x, y, 0xFFFFFF);
            }
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
