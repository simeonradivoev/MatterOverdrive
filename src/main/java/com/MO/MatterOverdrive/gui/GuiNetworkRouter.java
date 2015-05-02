package com.MO.MatterOverdrive.gui;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.container.ContainerNetworkRouter;
import com.MO.MatterOverdrive.gui.element.MOElementButton;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkRouter;
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
        super(new ContainerNetworkRouter(inventoryPlayer,entity),entity);
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
