package com.MO.MatterOverdrive.container;

import com.MO.MatterOverdrive.container.slot.SlotStarMap;
import com.MO.MatterOverdrive.tile.TileEntityMachineStarMap;
import com.MO.MatterOverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 6/15/2015.
 */
public class ContainerStarMap extends ContainerMachine<TileEntityMachineStarMap>
{
    public ContainerStarMap()
    {
        super();
    }

    public ContainerStarMap(InventoryPlayer inventory,TileEntityMachineStarMap machine)
    {
        super(inventory,machine);
    }

    @Override
    protected void init(InventoryPlayer inventory)
    {
        if (machine.getInventory() != null) {
            for (int i = 0; i < machine.getSizeInventory(); i++) {
                addSlotToContainer(new SlotStarMap(machine, i,inventory.player));
            }
        }

        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 270, true, true);
    }
}
