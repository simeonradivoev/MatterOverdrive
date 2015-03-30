package com.MO.MatterOverdrive.container;

import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.util.helpers.EnergyHelper;
import com.MO.MatterOverdrive.container.slot.SlotDatabase;
import com.MO.MatterOverdrive.container.slot.SlotPatternStorage;
import com.MO.MatterOverdrive.tile.TileEntityMachinePatternStorage;
import com.MO.MatterOverdrive.util.MOContainerHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/27/2015.
 */
public class ContainerPatternStorage extends MOBaseContainer
{
    TileEntityMachinePatternStorage patternStorage;
    private int lastEnergy;

    public ContainerPatternStorage(InventoryPlayer inventoryPlayer,TileEntityMachinePatternStorage patternStorage)
    {
        this.addSlotToContainer(new SlotDatabase(patternStorage,patternStorage.input_slot,8,52));
        this.addSlotToContainer(new SlotEnergy(patternStorage,patternStorage.getEnergySlotID(),8,79));

        for (int x = 0; x < 3;x++)
        {
            this.addSlotToContainer(new SlotPatternStorage(patternStorage,patternStorage.input_slot + 1 + x ,x * 24 + 77,37));
        }
        for (int x = 0; x < 3;x++)
        {
            this.addSlotToContainer(new SlotPatternStorage(patternStorage,patternStorage.input_slot + 1 + x + 3,x * 24 + 77,24 + 37));
        }

        MOContainerHelper.AddPlayerSlots(inventoryPlayer,this,45, 92);
        this.patternStorage = patternStorage;
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(int i = 0;i < this.crafters.size();i++)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if(this.lastEnergy != this.patternStorage.getEnergyStored(ForgeDirection.DOWN))
            {
                icrafting.sendProgressBarUpdate(this, 0, this.patternStorage.getEnergyStored(ForgeDirection.DOWN));
            }

            this.lastEnergy = this.patternStorage.getEnergyStored(ForgeDirection.DOWN);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        if (slot == 0)
            this.patternStorage.setEnergyStored(newValue);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotID);
        int last = this.patternStorage.getInventory().getLastSlotId();

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotID == patternStorage.input_slot)
            {
                if (!this.mergeItemStack(itemstack1, last+1, last+36+1, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);

            }else if(slotID == patternStorage.getEnergySlotID())
            {
                if (!this.mergeItemStack(itemstack1, last+1, last+36+1, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if(slotID > last)
            {
                if(MatterHelper.isMatterScanner(itemstack1))
                {
                    if(!this.mergeItemStack(itemstack1, this.patternStorage.input_slot, this.patternStorage.input_slot+1, false))
                    {
                        return null;
                    }
                }
                else if(EnergyHelper.isEnergyContainerItem(itemstack1))
                {
                    if(!this.mergeItemStack(itemstack1, this.patternStorage.getEnergySlotID(), this.patternStorage.getEnergySlotID()+1, false))
                    {
                        return null;
                    }
                }else if(MatterHelper.isMatterPatternStorage(itemstack1))
                {
                    if(!this.mergeItemStack(itemstack1, this.patternStorage.input_slot+1, this.patternStorage.getEnergySlotID(), false))
                    {
                        return null;
                    }
                }else if (slotID >= last+1 && slotID < last+28)
                {
                    if (!this.mergeItemStack(itemstack1, last+28, last+37, false))
                    {
                        return null;
                    }
                }else if (slotID >= last+28 && slotID < last+37 && !this.mergeItemStack(itemstack1, last+1, last+28, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, last+1, last+37, false))
            {
                return null;
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

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }
}
