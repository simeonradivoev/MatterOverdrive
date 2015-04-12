package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.container.ContainerNetworkController;
import com.MO.MatterOverdrive.gui.element.ElementGrid;
import com.MO.MatterOverdrive.gui.element.MatterConnectionElement;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkController;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 3/13/2015.
 */
public class GuiNetworkController extends MOGuiMachine<TileEntityMachineNetworkController>
{
    ElementGrid grid;

    public GuiNetworkController(InventoryPlayer inventoryPlayer,TileEntityMachineNetworkController entity)
    {
        super(new ContainerNetworkController(inventoryPlayer,entity),entity);
        name = "network_controller";
        grid = new ElementGrid(this,45,30,168,100,168);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        homePage.addElement(grid);
        System.out.println("Size of network when GUI opened: " + machine.getNetwork().getConnections().size());

        if(machine.connectionsInfoMap != null) {
            for (int i : machine.connectionsInfoMap.keySet()) {
                grid.addElement(new MatterConnectionElement(this, i, machine.connectionsInfoMap.get(i)));
            }
        }

        AddPlayerSlots(inventorySlots,this,false,true);
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
