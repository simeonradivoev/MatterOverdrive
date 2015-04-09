package com.MO.MatterOverdrive.tile;

import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.api.matter.IMatterPatternStorage;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.DatabaseSlot;
import com.MO.MatterOverdrive.data.inventory.PatternStorageSlot;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/27/2015.
 */
public class TileEntityMachinePatternStorage extends MOTileEntityMachineEnergy implements IMatterDatabase
{
    public static final int ENERGY_CAPACITY = 64000;
    public static final int ENERGY_TRANSFER = 128;
    public int input_slot;
    public int[] pattern_storage_slots;

    public TileEntityMachinePatternStorage()
    {
        super(4);
        this.energyStorage.setCapacity(ENERGY_CAPACITY);
        this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
        this.energyStorage.setMaxReceive(ENERGY_TRANSFER);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if(!worldObj.isRemote) {
            if (energyStorage.getEnergyStored() > 0) {
                manageLinking();
            }
        }
    }

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        pattern_storage_slots = new int[6];
        input_slot = inventory.AddSlot(new DatabaseSlot(true));

        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            pattern_storage_slots[i] = inventory.AddSlot(new PatternStorageSlot(false));
        }

        super.RegisterSlots(inventory);
    }

    protected void manageLinking()
    {
        if(MatterHelper.isMatterScanner(inventory.getStackInSlot(input_slot)))
        {
            MatterScanner.link(worldObj, xCoord, yCoord, zCoord, inventory.getStackInSlot(input_slot));
        }
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    @Override
    public boolean canConnectToNetwork(ForgeDirection direction)
    {
        return true;
    }

    //region Database functions
    @Override
    public NBTTagList getItemsAsNBT()
    {
        NBTTagList list = new NBTTagList();
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                NBTTagList l = MatterDatabaseHelper.GetItemsTagList(inventory.getStackInSlot(pattern_storage_slots[i]));
                if(l != null)
                {
                    for (int t = 0;t < l.tagCount();t++)
                    {
                        list.appendTag(l.getCompoundTagAt(t));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public ItemStack[] getItems() {
        return new ItemStack[0];
    }

    @Override
    public boolean hasItem(int id)
    {
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                boolean hasItem = MatterDatabaseHelper.HasItem(inventory.getStackInSlot(pattern_storage_slots[i]), id);
                if(hasItem)
                    return hasItem;
            }
        }
        return false;
    }

    @Override
    public boolean hasItem(String id)
    {
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                boolean hasItem = MatterDatabaseHelper.HasItem(inventory.getStackInSlot(pattern_storage_slots[i]), id);
                if(hasItem)
                    return hasItem;
            }
        }
        return false;
    }

    @Override
    public boolean hasItem(ItemStack item)
    {
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                boolean hasItem = MatterDatabaseHelper.HasItem(inventory.getStackInSlot(pattern_storage_slots[i]), item);
                if(hasItem)
                    return hasItem;
            }
        }
        return false;
    }

    @Override
    public boolean addItem(ItemStack itemStack,int initialAmount)
    {
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                IMatterPatternStorage storage = (IMatterPatternStorage)inventory.getStackInSlot(pattern_storage_slots[i]).getItem();
                if(storage.addItem(inventory.getStackInSlot(pattern_storage_slots[i]),itemStack,initialAmount))
                {
                    forceClientUpdate = true;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound getItemAsNBT(ItemStack item)
    {
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            if(MatterHelper.isMatterPatternStorage(inventory.getStackInSlot(pattern_storage_slots[i])))
            {
                NBTTagCompound hasItem = MatterDatabaseHelper.GetItemAsNBT(inventory.getStackInSlot(pattern_storage_slots[i]), item);
                if(hasItem != null)
                    return hasItem;
            }
        }
        return null;
    }

    @Override
    public ItemStack[] getPatternStorageList()
    {
        ItemStack[] patternsDrives = new ItemStack[pattern_storage_slots.length];
        for (int i = 0;i < pattern_storage_slots.length;i++)
        {
            patternsDrives[i] = getStackInSlot(pattern_storage_slots[i]);
        }
        return patternsDrives;
    }
    //endregion

    //region Invetory Functions
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if(side == 1)
        {
            return new int[]{input_slot};
        }
        else
        {
            return pattern_storage_slots;
        }
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return true;
    }
    //endregion
}
