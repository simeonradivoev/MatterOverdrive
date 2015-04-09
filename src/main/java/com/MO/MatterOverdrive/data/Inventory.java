package com.MO.MatterOverdrive.data;

import com.MO.MatterOverdrive.data.inventory.Slot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Simeon on 3/16/2015.
 */
public class Inventory implements IInventory
{
    List<Slot> slots;
    TileEntity entity;
    String name;
    IUseableCondition useableCondition;

    //region Constructors
    public Inventory(TileEntity entity,String name)
    {
        this(entity,name,new ArrayList<Slot>());
    }

    public Inventory(TileEntity entity,String name,Collection<Slot> slots)
    {
        this(entity,name,slots,null);
    }

    public Inventory(TileEntity entity,String name,Collection<Slot> slots,IUseableCondition useableCondition)
    {
        this.slots = new ArrayList<Slot>(slots);
        this.entity = entity;
        this.name = name;
        this.useableCondition = useableCondition;
    }
    //endregion

    public int AddSlot(Slot slot)
    {
        if(slots.add(slot))
        {
            slot.setId(slots.size()-1);
            return slots.size()-1;
        }
        return 0;
    }

    public void setUseableCondition(IUseableCondition condition)
    {
        this.useableCondition = condition;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = compound.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            setInventorySlotContents(b0, ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
        {
            if (getStackInSlot(i) != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                getStackInSlot(i).writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        compound.setTag("Items", nbttaglist);
    }

    @Override
    public int getSizeInventory()
    {
        return slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if(slot >= 0 && slot < getSizeInventory())
        {
            return slots.get(slot).getItem();
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int size)
    {
        if (this.slots.get(slot) != null && this.slots.get(slot).getItem() != null)
        {
            ItemStack itemstack;

            if (this.slots.get(slot).getItem().stackSize <= size)
            {
                itemstack = this.slots.get(slot).getItem();
                this.slots.get(slot).setItem(null);

                entity.updateContainingBlockInfo();
                return itemstack;
            }
            else
            {
                itemstack = this.slots.get(slot).getItem().splitStack(size);

                if (this.slots.get(slot).getItem().stackSize == 0)
                {
                    this.slots.get(slot).setItem(null);
                }

                entity.updateContainingBlockInfo();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if(this.slots.get(slot) != null)
        {
            ItemStack itemstack = this.slots.get(slot).getItem();
            this.slots.set(slot, null);
            return itemstack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack item)
    {
        this.slots.get(slot).setItem(item);

        if(item != null && item.stackSize > this.getInventoryStackLimit())
        {
            item.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return this.name;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return name != null && name != "";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
        if(this.entity != null)
        {
            this.entity.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        if(useableCondition != null)
        {
            return useableCondition.usableByPlayer(player);
        }
        return entity.getWorldObj().getTileEntity(entity.xCoord, entity.yCoord, entity.zCoord) != entity ? false : player.getDistanceSq((double)entity.xCoord + 0.5D, (double)entity.yCoord + 0.5D, (double)entity.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        if (slot >= 0 && slot < getSizeInventory() && slots.get(slot) != null)
        {
            return slots.get(slot).isValidForSlot(item);
        }
        return true;
    }

    public Slot getSlot(int slotID) {
        return slots.get(slotID);
    }

    public int getLastSlotId()
    {
        return slots.size()-1;
    }

    public List<Slot> getSlots() {
        return slots;
    }
}
