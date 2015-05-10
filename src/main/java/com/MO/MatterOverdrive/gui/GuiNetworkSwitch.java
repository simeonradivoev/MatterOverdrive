package com.MO.MatterOverdrive.gui;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.container.ContainerNetworkSwitch;
import com.MO.MatterOverdrive.gui.element.ElementConnections;
import com.MO.MatterOverdrive.gui.element.MOElementButton;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkSwitch;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

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
        homePage.addElement(connections);
        AddHotbarPlayerSlots(inventorySlots, this);
    }
}
