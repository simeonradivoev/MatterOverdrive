package com.MO.MatterOverdrive.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.lib.util.helpers.EnergyHelper;

import com.MO.MatterOverdrive.container.slot.SlotMatter;
import com.MO.MatterOverdrive.tile.TileEntityMachineDecomposer;
import com.MO.MatterOverdrive.util.MatterHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerDecomposer extends Container
{
	private TileEntityMachineDecomposer decomposer;
	private float lastDecomposeProgress;
	private int lastMatter;
	private int lastEnergy;
	
	public ContainerDecomposer(InventoryPlayer inventory,TileEntityMachineDecomposer tileentity)
	{
		this.decomposer = tileentity;
		
		this.addSlotToContainer(new SlotMatter(tileentity,decomposer.INPUT_SLOT_ID,8,52));
        this.addSlotToContainer(new SlotRemoveOnly(tileentity,decomposer.OUTPUT_SLOT_ID,129,52));
		this.addSlotToContainer(new SlotEnergy(tileentity,this.decomposer.getEnergySlotID(),8,79));
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0;j < 9;j++)
			{
				this.addSlotToContainer(new Slot(inventory,j + i*9 + 9,45 + j*18,92 + i*18));
			}
		}
		
		for(int i = 0;i < 9;i++)
		{
			this.addSlotToContainer(new Slot(inventory,i,45 + i*18,150));
		}
	}
	
	public void addCrafingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.decomposer.decomposeTime);
		icrafting.sendProgressBarUpdate(this, 1, this.decomposer.getMatterStored());
		icrafting.sendProgressBarUpdate(this, 2, this.decomposer.getEnergyStored(ForgeDirection.DOWN));
	}
	
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0;i < this.crafters.size();i++)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if(this.lastDecomposeProgress != this.decomposer.decomposeProgress)
			{
				icrafting.sendProgressBarUpdate(this, 0, this.decomposer.decomposeProgress);
			}
			
			if(this.lastMatter != this.decomposer.getMatterStored())
			{
				icrafting.sendProgressBarUpdate(this, 1, this.decomposer.getMatterStored());
			}
			
			if(this.lastEnergy != this.decomposer.getEnergyStored(ForgeDirection.DOWN))
			{
				icrafting.sendProgressBarUpdate(this, 2, this.decomposer.getEnergyStored(ForgeDirection.DOWN));
			}

			this.lastDecomposeProgress = this.decomposer.decomposeProgress;
			this.lastMatter = this.decomposer.getMatterStored();
			this.lastEnergy = this.decomposer.getEnergyStored(ForgeDirection.DOWN);
		}
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotID);
        int last = this.decomposer.getInventory().getLastSlotId();
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(slotID == decomposer.INPUT_SLOT_ID)
			{
				if (!this.mergeItemStack(itemstack1, last+1, last+36+1, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}else if(slotID == decomposer.OUTPUT_SLOT_ID)
            {
                if (!this.mergeItemStack(itemstack1, last+1, last+36+1, true))
                {
                    return null;
                }
            }
            else if(slotID > last)
			{
				if(MatterHelper.containsMatter(itemstack1))
				{
					if(!this.mergeItemStack(itemstack1, decomposer.INPUT_SLOT_ID, decomposer.INPUT_SLOT_ID+1, false))
					{
						return null;
					}
				}
				else if(EnergyHelper.isEnergyContainerItem(itemstack1))
				{
					if(!this.mergeItemStack(itemstack1, this.decomposer.getEnergySlotID(), this.decomposer.getEnergySlotID()+1, false))
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
			else if (!this.mergeItemStack(itemstack1, this.decomposer.getEnergySlotID()+1, last+37, false))
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
			this.decomposer.decomposeProgress = newValue;
		if(slot == 1)
			this.decomposer.setMatterStored(newValue);
		if(slot == 2)
			this.decomposer.setEnergyStored(newValue);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return true;
	}
}
