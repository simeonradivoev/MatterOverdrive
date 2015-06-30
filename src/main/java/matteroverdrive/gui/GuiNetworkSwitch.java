package matteroverdrive.gui;

import matteroverdrive.container.ContainerNetworkSwitch;
import matteroverdrive.gui.element.ElementConnections;
import matteroverdrive.tile.TileEntityMachineNetworkSwitch;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 5/1/2015.
 */
public class GuiNetworkSwitch extends MOGuiMachine<TileEntityMachineNetworkSwitch>
{
    ElementConnections connections;

    public GuiNetworkSwitch(InventoryPlayer inventoryPlayer, TileEntityMachineNetworkSwitch entity)
    {
        super(new ContainerNetworkSwitch(inventoryPlayer, entity), entity);
        name = "network_switch";
        connections = new ElementConnections(this,50,42,xSize - 74,ySize,machine);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        pages.get(0).addElement(connections);
        AddHotbarPlayerSlots(inventorySlots, this);
    }
}
