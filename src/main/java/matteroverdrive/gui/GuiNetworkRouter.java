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

package matteroverdrive.gui;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.container.ContainerFactory;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.tile.TileEntityMachineNetworkRouter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 3/13/2015.
 */
public class GuiNetworkRouter extends MOGuiMachine<TileEntityMachineNetworkRouter>
{
    public GuiNetworkRouter(InventoryPlayer inventoryPlayer, TileEntityMachineNetworkRouter entity)
    {
        super(ContainerFactory.createMachineContainer(entity,inventoryPlayer),entity);
        name = "network_router";
    }

    @Override
    public void initGui()
    {
        super.initGui();
        AddHotbarPlayerSlots(inventorySlots,this);
    }

    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        super.drawGuiContainerForegroundLayer(var1,var2);
        drawConnections();
    }

    protected void drawConnections()
    {
        for (int i = 0;i < 6;i++)
        {
            BlockPosition connection = machine.getConnection(i);
            GL11.glColor3f(1,1,1);
            int x = 50;
            int y = 42 + i * 19;

            if (connection != null)
            {
                String info = ForgeDirection.getOrientation(i).name() + " : " + connection.getTileEntity(machine.getWorldObj()).getBlockType().getLocalizedName();

                int width = fontRendererObj.getStringWidth(info);
                MOElementButton.NORMAL_TEXTURE.Render(x - 6,y - 6,width + 12,19);
                fontRendererObj.drawString(info,x,y,0xFFFFFF);
            }
            else
            {
                MOElementButton.HOVER_TEXTURE_DARK.Render(x - 6,y - 6,80,19);
                fontRendererObj.drawString(ForgeDirection.getOrientation(i).name() + " : None", x, y, 0xFFFFFF);
            }
        }
    }
}
