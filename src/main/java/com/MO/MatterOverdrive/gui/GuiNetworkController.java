package com.MO.MatterOverdrive.gui;

import com.MO.MatterOverdrive.container.ContainerNetworkController;
import com.MO.MatterOverdrive.gui.element.ElementGrid;
import com.MO.MatterOverdrive.gui.element.MatterConnectionElement;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkController;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 3/13/2015.
 */
public class GuiNetworkController extends MOGuiBase
{

    TileEntityMachineNetworkController controller;
    ElementGrid grid;

    public GuiNetworkController(InventoryPlayer inventoryPlayer,TileEntityMachineNetworkController entity)
    {
        super(new ContainerNetworkController(inventoryPlayer,entity));
        this.controller = entity;
        grid = new ElementGrid(this,45,30,64,149,137);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.addElement(grid);
        grid.clear();
        System.out.println("Size of network when GUI opened: " + controller.getNetwork().getConnections().size());

        if(controller.connectionsInfoMap != null) {
            for (int i : controller.connectionsInfoMap.keySet()) {
                grid.add(new MatterConnectionElement(i, controller.connectionsInfoMap.get(i)));
            }
        }
    }

    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
        drawConnections();
        this.drawElements(0.0F, true);
        this.drawTabs(0.0F, true);
    }

    protected void drawConnections()
    {

    }
}
