package com.MO.MatterOverdrive.container;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.container.slot.MOSlot;
import com.MO.MatterOverdrive.container.slot.SlotWeaponModule;
import com.MO.MatterOverdrive.tile.TileEntityWeaponStation;
import com.MO.MatterOverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Created by Simeon on 4/13/2015.
 */
public class ContainerWeaponStation extends ContainerMachine<TileEntityWeaponStation>
{
    public ContainerWeaponStation(InventoryPlayer playerInventory, TileEntityWeaponStation machine)
    {
        this.machine = machine;
        init(playerInventory);
    }

    @Override
    protected void init(InventoryPlayer inventory)
    {
        addSlotToContainer(new MOSlot(machine, machine.INPUT_SLOT, 8, 55));
        for (int i = 0;i < 5;i++)
        {
            addSlotToContainer(new SlotWeaponModule(machine, i,0,0, i));
        }

        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 150, true, true);
    }

}
