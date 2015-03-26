package com.MO.MatterOverdrive.container;

import com.MO.MatterOverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.lib.util.helpers.EnergyHelper;

import com.MO.MatterOverdrive.gui.slot.SlotDatabase;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;
import com.MO.MatterOverdrive.util.MatterHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerReplicator extends MOBaseContainer
{
	private TileEntityMachineReplicator replicator;
	private int lastReplicateProgress;
	private int lastMatter;
	private int lastEnergy;
	
	public ContainerReplicator(InventoryPlayer inventory,TileEntityMachineReplicator tileentity)
	{
		this.replicator = tileentity;
		
		this.addSlotToContainer(new SlotRemoveOnly(tileentity,replicator.OUTPUT_SLOT_ID,70,52));
        this.addSlotToContainer(new SlotRemoveOnly(tileentity,replicator.SECOUND_OUTPUT_SLOT_ID,96,52));
		this.addSlotToContainer(new SlotDatabase(tileentity,replicator.DATABASE_SLOT_ID,8,52));
		this.addSlotToContainer(new SlotEnergy(tileentity,this.replicator.getEnergySlotID(),8,79));

        MOContainerHelper.AddPlayerSlots(inventory,this,45,92);
	}
	
	public void addCrafingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.replicator.replicateTime);
		icrafting.sendProgressBarUpdate(this, 1, this.replicator.getMatterStored());
		icrafting.sendProgressBarUpdate(this, 2, this.replicator.getEnergyStored(ForgeDirection.DOWN));
	}
	
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0;i < this.crafters.size();i++)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);
			
			if(this.lastReplicateProgress != this.replicator.replicateProgress)
			{
				icrafting.sendProgressBarUpdate(this, 0, this.replicator.replicateProgress);
			}
			
			if(this.lastMatter != this.replicator.getMatterStored())
			{
				icrafting.sendProgressBarUpdate(this, 1, this.replicator.getMatterStored());
			}
			
			if(this.lastEnergy != this.replicator.getEnergyStored(ForgeDirection.DOWN))
			{
				icrafting.sendProgressBarUpdate(this, 2, this.replicator.getEnergyStored(ForgeDirection.DOWN));
			}

			this.lastReplicateProgress = this.replicator.replicateProgress;
			this.lastMatter = this.replicator.getMatterStored();
			this.lastEnergy = this.replicator.getEnergyStored(ForgeDirection.DOWN);
		}
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotID);
        int last = this.replicator.getInventory().getLastSlotId();
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(slotID == replicator.OUTPUT_SLOT_ID || slotID == replicator.SECOUND_OUTPUT_SLOT_ID)
			{
				if (!this.mergeItemStack(itemstack1, last+1, last+36+1, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}else if(slotID > this.replicator.getEnergySlotID())
			{
				if(EnergyHelper.isEnergyContainerItem(itemstack1))
				{
					if(!this.mergeItemStack(itemstack1, this.replicator.getEnergySlotID(), this.replicator.getEnergySlotID()+1, false))
					{
						return null;
					}
				}else if(MatterHelper.isDatabaseItem(itemstack1))
				{
					if(!this.mergeItemStack(itemstack1, replicator.DATABASE_SLOT_ID, replicator.DATABASE_SLOT_ID+1, false))
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
			this.replicator.replicateProgress = newValue;
		if(slot == 1)
			this.replicator.setMatterStored(newValue);
		if(slot == 2)
			this.replicator.setEnergyStored(newValue);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return true;
	}
}
