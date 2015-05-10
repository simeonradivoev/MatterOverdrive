package com.MO.MatterOverdrive.tile;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.inventory.UpgradeTypes;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.ItemInventoryWrapper;
import com.MO.MatterOverdrive.data.inventory.ModuleSlot;
import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.data.inventory.UpgradeSlot;
import com.MO.MatterOverdrive.data.inventory.WeaponSlot;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/13/2015.
 */
public class TileEntityWeaponStation extends MOTileEntityMachine
{
    public int INPUT_SLOT;
    public int BATTERY_MODULE;
    public int COLOR_MODULE;
    public int BARREL_MODULE;
    public int SIGHTS_MODULE;
    public int OTHER_MODULE;

    public TileEntityWeaponStation()
    {
        super(0);
    }

    @Override
    protected void RegisterSlots(Inventory inventory)
    {
        BATTERY_MODULE = inventory.AddSlot(new ModuleSlot(false, Reference.MODULE_BATTERY));
        COLOR_MODULE = inventory.AddSlot(new ModuleSlot(false,Reference.MODULE_COLOR));
        BARREL_MODULE = inventory.AddSlot(new ModuleSlot(false,Reference.MODULE_BARREL));
        SIGHTS_MODULE = inventory.AddSlot(new ModuleSlot(false,Reference.MODULE_SIGHTS));
        OTHER_MODULE = inventory.AddSlot(new ModuleSlot(false,Reference.MODULE_OTHER));
        INPUT_SLOT = inventory.AddSlot(new WeaponSlot(true));
        redstoneMode = Reference.MODE_REDSTONE_LOW;
        super.RegisterSlots(inventory);
    }

    public IInventory getActiveInventory()
    {
        if (inventory.getSlot(INPUT_SLOT).getItem() != null && MatterHelper.isWeapon(inventory.getSlot(INPUT_SLOT).getItem()))
        {
            return new ItemInventoryWrapper(inventory.getSlot(INPUT_SLOT).getItem(),5);
        }
        return inventory;
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

    //region Inventory Functions
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot != INPUT_SLOT)
        {
            return getActiveInventory().getStackInSlot(slot);
        }else
        {
            return super.getStackInSlot(slot);
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int size)
    {
        if (slot != INPUT_SLOT)
        {
            return getActiveInventory().decrStackSize(slot, size);
        }else
        {
            return super.decrStackSize(slot, size);
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (slot != INPUT_SLOT)
        {
            return getActiveInventory().getStackInSlotOnClosing(slot);
        }else
        {
            return super.getStackInSlotOnClosing(slot);
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        if (slot != INPUT_SLOT)
        {
            getActiveInventory().setInventorySlotContents(slot, itemStack);
        }else
        {
            super.setInventorySlotContents(slot,itemStack);
        }
    }
    //endregion

    //region Upgrades
    @Override
    public boolean isAffectedBy(UpgradeTypes type)
    {
        return false;
    }
    //endregion
}
