/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.tile;

import cofh.api.energy.IEnergyStorage;
import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.DatabaseSlot;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.components.MatterNetworkComponentAnalyzer;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/16/2015.
 */
public class TileEntityMachineMatterAnalyzer extends MOTileEntityMachineEnergy implements ISidedInventory, IMatterNetworkDispatcher<MatterNetworkTaskStorePattern>, IMatterNetworkClient
{
    public static final int BROADCAST_DELAY = 60;
    public static final int BROADCAST_WEATING_DELAY = 180;
    public static final int VALID_LOCATION_CHECK_DELAY = 200;

    public static final int PROGRESS_AMOUNT_PER_ITEM = 20;
    public static final int ENERGY_STORAGE = 512000;
    public static final int ENERGY_TRANSFER = 512;
    public static final int ANALYZE_SPEED = 800;
    public static final int ENERGY_DRAIN_PER_ITEM = 64000;

    public int input_slot = 0;
    public int database_slot = 1;
    public int analyzeTime;
    private boolean isActive = false;
    private MatterNetworkTaskQueue<MatterNetworkTaskStorePattern> taskQueueSending;
    private MatterNetworkComponentAnalyzer networkComponent;
    private boolean hasValidPatternDestination;

    public TileEntityMachineMatterAnalyzer()
    {
        super(4);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
        this.energyStorage.setMaxReceive(ENERGY_TRANSFER);
        taskQueueSending = new MatterNetworkTaskQueue<>(this,1);
        redstoneMode = Reference.MODE_REDSTONE_LOW;
        networkComponent = new MatterNetworkComponentAnalyzer(this);

    }

    @Override
    public void RegisterSlots(Inventory inventory)
    {
        input_slot = inventory.AddSlot(new Slot(true));
        database_slot = inventory.AddSlot(new DatabaseSlot(true));

        super.RegisterSlots(inventory);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        manageAnalyze();
    }

    protected void manageAnalyze()
    {
        if(!worldObj.isRemote)
        {
            boolean isAnalyzing = isAnalyzing();

            if (isAnalyzing && this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick())
            {
                this.energyStorage.extractEnergy(getEnergyDrainPerTick(),false);

                if (analyzeTime < getSpeed())
                {
                    analyzeTime++;
                } else {
                    analyzeItem();
                    analyzeTime = 0;
                }

                isActive = true;
            }

            if (!isAnalyzing())
            {
                isActive = false;
                analyzeTime = 0;
            }
        }
    }

    public boolean isAnalyzing()
    {
        if (getRedstoneActive() && inventory.getSlot(input_slot).getItem() != null)
        {
            if (inventory.getSlot(database_slot).getItem() != null)
            {
                //get the Matterscanner destination
                return MatterHelper.getMatterAmountFromItem(inventory.getStackInSlot(input_slot)) > 0 && hasConnectionToPatterns();
            }
            else
            {
                if (taskQueueSending.remaintingCapacity() > 0 && hasValidPatternDestination)
                {
                    return  true;
                }
            }
        }
        return false;
    }

    public boolean hasConnectionToPatterns()
    {
        if(MatterHelper.isMatterScanner(inventory.getStackInSlot(database_slot)))
        {
            //the matter scanner pattern storage
            IMatterDatabase database = MatterScanner.getLink(worldObj, inventory.getStackInSlot(database_slot));
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

        return false;
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

        if(database != null)
        {
            if (database.addItem(itemStack,PROGRESS_AMOUNT_PER_ITEM,false,null))
            {
                SoundHandler.PlaySoundAt(worldObj, "scanner_success", xCoord, yCoord, zCoord);
            }
            else
            {
                //if the scanner cannot take the item for some reason
                //then just queue the analyzed item as a task
                MatterNetworkTaskStorePattern storePattern = new MatterNetworkTaskStorePattern(this,itemStack,PROGRESS_AMOUNT_PER_ITEM);
                taskQueueSending.queue(storePattern);
            }
        }
        else
        {
            MatterNetworkTaskStorePattern storePattern = new MatterNetworkTaskStorePattern(this,itemStack,PROGRESS_AMOUNT_PER_ITEM);
            taskQueueSending.queue(storePattern);
        }

        decrStackSize(input_slot, 1);
        forceClientUpdate = true;
        markDirty();
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type == UpgradeTypes.PowerUsage || type == UpgradeTypes.PowerStorage || type == UpgradeTypes.Fail || type == UpgradeTypes.Output || type == UpgradeTypes.Speed;
    }

    //region NBT
    @Override
    public void readCustomNBT(NBTTagCompound tagCompound)
    {
        super.readCustomNBT(tagCompound);
        analyzeTime = tagCompound.getShort("AnalyzeTime");
        isActive = tagCompound.getBoolean("IsActive");
        taskQueueSending.readFromNBT(tagCompound);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tagCompound)
    {
        super.writeCustomNBT(tagCompound);
        tagCompound.setShort("AnalyzeTime", (short) analyzeTime);
        tagCompound.setBoolean("IsActive", isActive);
        taskQueueSending.writeToNBT(tagCompound);
    }
    //endregion

    //region Inventory Methods
    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        return true;
    }

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
    //endregion

    //region Matter Network Functions
    @Override
    public BlockPosition getPosition()
    {
        return new BlockPosition(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side) {
        int meta = worldObj.getBlockMetadata(xCoord,yCoord,zCoord);
        if (BlockHelper.getOppositeSide(meta) == side.ordinal())
        {
            return true;
        }
        return false;
    }

    @Override
    public int onNetworkTick(World world,TickEvent.Phase phase)
    {
        return networkComponent.onNetworkTick(world,phase);
    }

    @Override
    public MatterNetworkTaskQueue<MatterNetworkTaskStorePattern> getTaskQueue(int id)
    {
        return taskQueueSending;
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        return networkComponent.canPreform(packet);
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from)
    {
        networkComponent.queuePacket(packet,from);
    }
    //endregion

    //region Events
    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public void onActiveChange()
    {
        ForceSync();
    }
    //endregion

    //region Getters and Setters
    public IEnergyStorage getEnergyStorage() {return energyStorage;}
    public int getSpeed() {return MathHelper.round(ANALYZE_SPEED * getUpgradeMultiply(UpgradeTypes.Speed));}
    public int getEnergyDrainPerTick() {return getEnergyDrainMax() / getSpeed();}
    public int getEnergyDrainMax() {return MathHelper.round(ENERGY_DRAIN_PER_ITEM * getUpgradeMultiply(UpgradeTypes.PowerUsage));}
    @Override
    public  boolean isActive() {return isActive;}
    @Override
    public String getSound() {return "analyzer";}
    @Override
    public boolean hasSound() {return true;}
    @Override
    public float soundVolume() { return 0.3f;}
    public void setHasValidPatternDestination(boolean hasValidPatternDestination){this.hasValidPatternDestination = hasValidPatternDestination;}
    public boolean getHasValidPatternDestination(){return hasValidPatternDestination;}
    //endregion
}
