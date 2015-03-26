package com.MO.MatterOverdrive.container;

import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Simeon on 3/13/2015.
 */
public class ContainerNetworkController extends Container
{
    TileEntityMachineNetworkController controller;

    public ContainerNetworkController(InventoryPlayer inventoryPlayer,TileEntityMachineNetworkController entity)
    {
        this.controller = entity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
