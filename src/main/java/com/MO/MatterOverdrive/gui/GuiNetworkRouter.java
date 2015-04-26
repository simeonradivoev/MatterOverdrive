package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.container.ContainerNetworkController;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkRouter;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 3/13/2015.
 */
public class GuiNetworkRouter extends MOGuiMachine<TileEntityMachineNetworkRouter>
{
    public GuiNetworkRouter(InventoryPlayer inventoryPlayer, TileEntityMachineNetworkRouter entity)
    {
        super(new ContainerNetworkController(inventoryPlayer,entity),entity);
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

    }
}
