package matteroverdrive.container;

import matteroverdrive.container.slot.MOSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * Created by Simeon on 3/16/2015.
 */
public abstract class MOBaseContainer extends Container
{
    public MOBaseContainer(){}

    public MOBaseContainer(InventoryPlayer inventoryPlayer) {}

    public Slot addSlotToContainer(Slot slot)
    {
        return super.addSlotToContainer(slot);
    }

    public MOSlot getSlotAt(int id)
    {
        return  (MOSlot)inventorySlots.get(id);
    }
}
