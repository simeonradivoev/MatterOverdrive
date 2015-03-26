package com.MO.MatterOverdrive.container;

import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.util.helpers.EnergyHelper;
import com.MO.MatterOverdrive.gui.slot.SlotDatabase;
import com.MO.MatterOverdrive.gui.slot.SlotMatter;
import com.MO.MatterOverdrive.tile.TileEntityMachineMatterAnalyzer;
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
 * Created by Simeon on 3/16/2015.
 */
public class ContainerMatterAnalyzer extends MOBaseContainer
{
    private TileEntityMachineMatterAnalyzer analyzer;
    private int lastAnalyzeTime;
    private int lastEnergy;

    public ContainerMatterAnalyzer(InventoryPlayer inventory,TileEntityMachineMatterAnalyzer analyzer)
    {
        this.analyzer = analyzer;

        this.addSlotToContainer(new SlotMatter(analyzer,analyzer.input_slot,8,52));
        this.addSlotToContainer(new SlotDatabase(analyzer,analyzer.database_slot,8,79));
        this.addSlotToContainer(new SlotEnergy(analyzer,analyzer.getEnergySlotID(),8,106));

        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 92);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(int i = 0;i < this.crafters.size();i++)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if(this.lastAnalyzeTime != this.analyzer.analyzeTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.analyzer.analyzeTime);
            }
            if(this.lastEnergy != this.analyzer.getEnergyStored(ForgeDirection.DOWN))
            {
                icrafting.sendProgressBarUpdate(this, 1, this.analyzer.getEnergyStored(ForgeDirection.DOWN));
            }

            this.lastAnalyzeTime = this.analyzer.analyzeTime;
            this.lastEnergy = this.analyzer.getEnergyStored(ForgeDirection.DOWN);
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotID);
        int last = this.analyzer.getInventory().getLastSlotId();

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotID == analyzer.input_slot)
            {
                if (!this.mergeItemStack(itemstack1, last+1, last+36+1, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);

            }else if(slotID == analyzer.database_slot || slotID == analyzer.getEnergySlotID())
            {
                if (!this.mergeItemStack(itemstack1, last+1, last+36+1, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if(slotID > last)
            {
                if(MatterHelper.getMatterAmountFromItem(itemstack1) > 0)
                {
                    if(!this.mergeItemStack(itemstack1, this.analyzer.input_slot, this.analyzer.input_slot+1, false))
                    {
                        return null;
                    }
                }
                else if(EnergyHelper.isEnergyContainerItem(itemstack1))
                {
                    if(!this.mergeItemStack(itemstack1, this.analyzer.getEnergySlotID(), this.analyzer.getEnergySlotID()+1, false))
                    {
                        return null;
                    }
                }else if(MatterHelper.isDatabaseItem(itemstack1))
                {
                    if(!this.mergeItemStack(itemstack1, this.analyzer.database_slot, this.analyzer.database_slot+1, false))
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

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        if(slot == 0)
            this.analyzer.analyzeTime = newValue;
        if (slot == 1)
            this.analyzer.setEnergyStored(newValue);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
