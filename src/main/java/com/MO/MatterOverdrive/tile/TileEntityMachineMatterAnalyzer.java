package com.MO.MatterOverdrive.tile;

import cofh.api.energy.IEnergyStorage;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.matter.IMatterDatabase;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.DatabaseSlot;
import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.handler.SoundHandler;
import com.MO.MatterOverdrive.items.MatterScanner;
import com.MO.MatterOverdrive.sound.MachineSound;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Simeon on 3/16/2015.
 */
public class TileEntityMachineMatterAnalyzer extends MOTileEntityMachineEnergy implements ISidedInventory
{
    public static final int PROGRESS_AMOUNT_PER_ITEM = 20;
    public static final int ENERGY_STORAGE = 512000;
    public static final int ENERGY_TRANSFER = 512;
    public static final int ANALYZE_SPEED = 800;
    public static final int POWER_DRAIN = 64;

    public int input_slot = 0;
    public int database_slot = 1;
    public int analyzeTime;
    private boolean isActive = false;

    public TileEntityMachineMatterAnalyzer()
    {
        super(4);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
        this.energyStorage.setMaxReceive(ENERGY_TRANSFER);
    }

    @Override
    public void RegisterSlots(Inventory inventory)
    {
        input_slot = inventory.AddSlot(new Slot(true));
        database_slot = inventory.AddSlot(new DatabaseSlot(true));

        super.RegisterSlots(inventory);
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
            boolean isAnalyzing = isAnalyzing();

            if (isAnalyzing && this.energyStorage.getEnergyStored() >= POWER_DRAIN)
            {
                this.energyStorage.extractEnergy(POWER_DRAIN,false);

                if (analyzeTime < ANALYZE_SPEED)
                {
                    analyzeTime++;
                } else {
                    analyzeItem();
                    analyzeTime = 0;
                }

                isActive = true;
            }

            if (!isAnalyzing)
            {
                isActive = false;
                analyzeTime = 0;
            }
        }
    }


    public boolean isAnalyzing()
    {
        return  inventory.getStackInSlot(input_slot) != null
                && MatterHelper.getMatterAmountFromItem(inventory.getStackInSlot(input_slot)) > 0
                && hasConnectionToPatterns();
    }

    @Override
    public void onActiveChange()
    {
        forceClientUpdate = true;
    }

    public boolean hasConnectionToPatterns()
    {
        if(inventory.getStackInSlot(database_slot) != null)
        {
            if(MatterHelper.isMatterScanner(inventory.getStackInSlot(database_slot)))
            {
                //the matter scanner pattern storage
                IMatterDatabase database = MatterScanner.getLink(worldObj,inventory.getStackInSlot(database_slot));
                if (database != null)
                {
                    if (database.hasItem(inventory.getStackInSlot(input_slot)))
                    {
                        NBTTagCompound itemAsNBT = database.getItemAsNBT(inventory.getStackInSlot(input_slot));
                        if(itemAsNBT != null)
                        {
                            if (MatterDatabaseHelper.GetProgressFromNBT(itemAsNBT) < MatterDatabaseHelper.MAX_ITEM_PROGRESS)
                            {
                                return true;
                            }
                        }
                    }
                    else if (MatterDatabaseHelper.getFirstFreePatternStorage(database) != null)
                    {
                        return true;
                    }
                }
            }
        }
        else if(network != null)
        {
            List<IMatterNetworkConnection> connections = network.getConnections();
            for (int i = 0;i < connections.size();i++)
            {
                if (connections.get(i) instanceof IMatterDatabase)
                {
                    IMatterDatabase database = (IMatterDatabase)connections.get(i);
                    NBTTagCompound itemNBT = database.getItemAsNBT(inventory.getStackInSlot(input_slot));

                    if (itemNBT != null)
                    {
                        if (MatterDatabaseHelper.GetProgressFromNBT(itemNBT) < MatterDatabaseHelper.MAX_ITEM_PROGRESS)
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if (MatterDatabaseHelper.getFirstFreePatternStorage(database) != null)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public  boolean isActive()
    {
        return isActive;
    }

    public void analyzeItem()
    {
        ItemStack scanner = inventory.getStackInSlot(database_slot);
        ItemStack itemStack = inventory.getStackInSlot(input_slot);
        IMatterDatabase database = null;

        //get the database from the scanner first
        if(scanner != null && MatterHelper.isMatterScanner(scanner))
        {
            database = MatterScanner.getLink(worldObj,scanner);
        }

        //get the first database on the network, if the scanner is offline
        if (database == null)
        {
            database = network.getFirstMatterDatabase();
        }

        if(database != null)
        {
            MatterDatabaseHelper.increaseProgress(database,itemStack,PROGRESS_AMOUNT_PER_ITEM);
            SoundHandler.PlaySoundAt(worldObj,"scanner_success",xCoord,yCoord,zCoord);
            decrStackSize(input_slot, 1);
            forceClientUpdate = true;
            markDirty();
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound tagCompound)
    {
        super.readCustomNBT(tagCompound);
        analyzeTime = tagCompound.getShort("AnalyzeTime");
        isActive = tagCompound.getBoolean("IsActive");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tagCompound)
    {
        super.writeCustomNBT(tagCompound);
        tagCompound.setShort("AnalyzeTime", (short) analyzeTime);
        tagCompound.setBoolean("IsActive",isActive);
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    //region Inventory Methods
    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if(side == 1)
        {
            return new int[]{input_slot,database_slot};
        }
        else
        {
            return new int[]{input_slot};
        }
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return true;
    }

    @Override
    public boolean canConnectToNetwork(ForgeDirection direction) {
        return true;
    }
    //endregion
}
