package com.MO.MatterOverdrive.tile;

import cofh.api.energy.IEnergyStorage;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.DatabaseSlot;
import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.sound.MachineSound;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/16/2015.
 */
public class TileEntityMachineMatterAnalyzer extends MOTileEntityMachineEnergy implements ISidedInventory
{
    public static final int ENERGY_STORAGE = 512000;
    public static final int ENERGY_TRANSFER = 512;
    public static final int ANALYZE_SPEED = 800;
    public static final int POWER_DRAIN = 64;

    public int input_slot = 0;
    public int database_slot = 1;
    public int analyzeTime;

    public TileEntityMachineMatterAnalyzer()
    {
        super(3);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
        this.energyStorage.setMaxReceive(ENERGY_TRANSFER);
    }

    @Override
    public void RegisterSlots(Inventory inventory)
    {
        input_slot = inventory.AddSlot(new Slot());
        database_slot = inventory.AddSlot(new DatabaseSlot());
    }

    public int getAnalyzeTimeFromScanner()
    {
        ItemStack scanner = inventory.getStackInSlot(database_slot);
        int progress = 0;

        if(scanner != null && scanner.hasTagCompound()) {
            progress = MatterDatabaseHelper.GetProgressFromNBT(scanner.getTagCompound());
        }

        return progress;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        manageAnalyze();
    }

    @Override
    public String getSound()
    {
        return "analyzer";
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public float soundVolume() { return 0.3f;}

    protected void manageAnalyze()
    {
        if(!worldObj.isRemote)
        {
            if (isAnalyzing() && this.energyStorage.getEnergyStored() >= POWER_DRAIN) {
                if (analyzeTime < ANALYZE_SPEED) {
                    analyzeTime++;
                } else {
                    analyzeItem();
                    analyzeTime = 0;
                }
            }
        }

        if(!isAnalyzing())
        {
            analyzeTime = 0;
        }
    }


    public boolean isAnalyzing()
    {
        return inventory.getStackInSlot(database_slot) != null
                && MatterHelper.isMatterScanner(inventory.getStackInSlot(database_slot))
                && MatterScanner.getLink(worldObj,inventory.getStackInSlot(database_slot)) != null
                && inventory.getStackInSlot(input_slot) != null
                && MatterHelper.getMatterAmountFromItem(inventory.getStackInSlot(input_slot)) > 0
                && MatterDatabaseHelper.GetItemProgress(inventory.getStackInSlot(database_slot), inventory.getStackInSlot(input_slot)) < MatterDatabaseHelper.MAX_ITEM_PROGRESS;
    }

    @Override
    public  boolean isActive()
    {
        return isAnalyzing() && this.energyStorage.getEnergyStored() >= POWER_DRAIN;
    }

    public void analyzeItem()
    {
        ItemStack scanner = inventory.getStackInSlot(database_slot);
        ItemStack itemStack = inventory.getStackInSlot(input_slot);

        if(scanner != null)
        {
            NBTTagCompound tagCompound = MatterDatabaseHelper.GetItemAsNBT(scanner, itemStack);

            if(tagCompound == null)
            {
                MatterDatabaseHelper.Register(scanner,itemStack,10);
            }
            else
            {
                MatterDatabaseHelper.IncreaseProgress(tagCompound,10);
            }

            MatterScanner.setSelectedIndex(scanner,itemStack.getItem().getUnlocalizedName());
            this.decrStackSize(input_slot,1);
            worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound tagCompound)
    {
        super.readCustomNBT(tagCompound);
        analyzeTime = tagCompound.getShort("AnalyzeTime");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tagCompound)
    {
        super.writeCustomNBT(tagCompound);
        tagCompound.setShort("AnalyzeTime",(short)analyzeTime);
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    //region Inventory Methods
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if(side == 0)
        {
            return new int[]{this.getEnergySlotID(),database_slot};
        }
        else
        {
            return new int[]{input_slot};
        }

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        return this.inventory.isItemValidForSlot(slot,item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean canConnectToNetwork(ForgeDirection direction) {
        return true;
    }
    //endregion
}
