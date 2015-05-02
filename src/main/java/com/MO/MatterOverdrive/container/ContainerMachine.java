package com.MO.MatterOverdrive.container;

import cofh.lib.util.helpers.InventoryHelper;
import com.MO.MatterOverdrive.container.slot.SlotUpgrade;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.UpgradeSlot;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/9/2015.
 */
public class ContainerMachine<T extends MOTileEntityMachine> extends MOBaseContainer
{
    protected T machine;

    public ContainerMachine()
    {
        super();
    }

    public ContainerMachine(InventoryPlayer inventory,T machine)
    {
        super(inventory);
        this.machine = machine;
        init(inventory);
    }

    protected void init(InventoryPlayer inventory)
    {
        AddUpgradeSlots(machine.getInventory(),77,52);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    public void AddUpgradeSlots(Inventory inventory,int x, int y)
    {
        int upgradeSlotIndex = 0;

        for (int i = 0;i < inventory.getSizeInventory();i++)
        {
            if (inventory.getSlot(i) instanceof UpgradeSlot)
            {
                addSlotToContainer(new SlotUpgrade(inventory,i,x + (upgradeSlotIndex % 5) * 24,y + (upgradeSlotIndex / 5) * 24));
                upgradeSlotIndex++;
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotID);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotID < machine.getSizeInventory())
            {
                putInPlayerInventory(itemstack1);
            }
            else if(slotID >= machine.getSizeInventory())
            {
                tryAndPutInMachineSlots(itemstack1,machine.getInventory());
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    protected boolean putInPlayerInventory(ItemStack itemStack)
    {
        return InventoryHelper.mergeItemStack(inventorySlots,itemStack,machine.getInventory().getSizeInventory(),36,false,true);
    }

    protected boolean tryAndPutInMachineSlots(ItemStack itemStack,Inventory inventory)
    {
        return InventoryHelper.mergeItemStack(inventorySlots,itemStack,0,inventory.getSizeInventory(),false,true);
    }
}
