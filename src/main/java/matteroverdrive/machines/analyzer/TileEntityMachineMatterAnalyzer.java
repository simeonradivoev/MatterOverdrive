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

package matteroverdrive.machines.analyzer;

import cofh.api.energy.IEnergyStorage;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.network.IMatterNetworkBroadcaster;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.BlockPos;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.data.inventory.DatabaseSlot;
import matteroverdrive.data.inventory.MatterSlot;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.analyzer.components.MatterNetworkComponentAnalyzer;
import matteroverdrive.machines.components.ComponentMatterNetworkConfigs;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkPacketQueue;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import matteroverdrive.tile.MOTileEntityMachineEnergy;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

import static matteroverdrive.util.MOBlockHelper.getOppositeSide;

/**
 * Created by Simeon on 3/16/2015.
 */
public class TileEntityMachineMatterAnalyzer extends MOTileEntityMachineEnergy implements ISidedInventory, IMatterNetworkDispatcher<MatterNetworkTaskStorePattern>, IMatterNetworkClient, IMatterNetworkBroadcaster
{
    public static final int BROADCAST_DELAY = 60;
    public static final int BROADCAST_WEATING_DELAY = 2;
    public static final int VALID_LOCATION_CHECK_DELAY = 200;

    public static final int PROGRESS_AMOUNT_PER_ITEM = 20;
    public static final int ENERGY_STORAGE = 512000;
    public static final int ENERGY_TRANSFER = 512;
    public static final int ANALYZE_SPEED = 800;
    public static final int ENERGY_DRAIN_PER_ITEM = 64000;

    public int input_slot = 0;
    public int database_slot = 1;
    public int analyzeTime;
    private MatterNetworkTaskQueue<MatterNetworkTaskStorePattern> taskQueueSending;
    private MatterNetworkComponentAnalyzer networkComponent;
    private ComponentMatterNetworkConfigs componentMatterNetworkConfigs;

    public TileEntityMachineMatterAnalyzer()
    {
        super(4);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
        this.energyStorage.setMaxReceive(ENERGY_TRANSFER);
        taskQueueSending = new MatterNetworkTaskQueue<>(this,1);
        playerSlotsHotbar = true;
        playerSlotsMain = true;
    }

    @Override
    public void RegisterSlots(Inventory inventory)
    {
        input_slot = inventory.AddSlot(new MatterSlot(true));
        database_slot = inventory.AddSlot(new DatabaseSlot(true));
        super.RegisterSlots(inventory);
    }

    @Override
    protected void registerComponents()
    {
        super.registerComponents();
        componentMatterNetworkConfigs = new ComponentMatterNetworkConfigs(this);
        networkComponent = new MatterNetworkComponentAnalyzer(this);

        addComponent(componentMatterNetworkConfigs);
        addComponent(networkComponent);
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
            if (isActive() && this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick() && networkComponent.getConnection() != null)
            {
                energyStorage.modifyEnergyStored(-getEnergyDrainPerTick());
                UpdateClientPower();

                if (analyzeTime < getSpeed())
                {
                    analyzeTime++;
                } else {
                    analyzeItem();
                    analyzeTime = 0;
                }
            }

            if (!isActive())
            {
                analyzeTime = 0;
            }
        }
    }

    public boolean isAnalyzing()
    {
        if (getRedstoneActive() && inventory.getSlot(input_slot).getItem() != null && getEnergyStored(ForgeDirection.UNKNOWN) > 0)
        {
            if (inventory.getSlot(database_slot).getItem() != null)
            {
                //get the Matterscanner destination
                return MatterHelper.getMatterAmountFromItem(inventory.getStackInSlot(input_slot)) > 0 && hasConnectionToPatterns();
            }
            else
            {
                if (taskQueueSending.remaintingCapacity() > 0)
                {
                    return true;
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
                    ItemPattern itemPattern = database.getPattern(inventory.getStackInSlot(input_slot));
                    if(itemPattern != null)
                    {
                        if (itemPattern.getProgress() < MatterDatabaseHelper.MAX_ITEM_PROGRESS)
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
                storePattern.setState(MatterNetworkTaskState.WAITING);
                taskQueueSending.queue(storePattern);
            }
        }
        else
        {
            MatterNetworkTaskStorePattern storePattern = new MatterNetworkTaskStorePattern(this,itemStack,PROGRESS_AMOUNT_PER_ITEM);
            storePattern.setState(MatterNetworkTaskState.WAITING);
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
    public void readCustomNBT(NBTTagCompound tagCompound, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(tagCompound, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            analyzeTime = tagCompound.getShort("AnalyzeTime");
            taskQueueSending.readFromNBT(tagCompound);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tagCompound, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {
        super.writeCustomNBT(tagCompound, categories, toDisk);
        if (categories.contains(MachineNBTCategory.DATA)) {
            tagCompound.setShort("AnalyzeTime", (short) analyzeTime);
            taskQueueSending.writeToNBT(tagCompound);
        }
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
    public BlockPos getPosition()
    {
        return new BlockPos(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side) {
        int meta = worldObj.getBlockMetadata(xCoord,yCoord,zCoord);
        return getOppositeSide(meta) == side.ordinal();
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
    public int getTaskQueueCount() {
        return 1;
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

    @Override
    public MatterNetworkPacketQueue<MatterNetworkPacket> getPacketQueue(int queueID)
    {
        return networkComponent.getPacketQueue(queueID);
    }

    @Override
    public int getPacketQueueCount() {
        return networkComponent.getPacketQueueCount();
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
        forceSync();
    }
    //endregion

    //region Getters and Setters
    public IEnergyStorage getEnergyStorage() {return energyStorage;}
    public int getSpeed() {return (int)Math.round(ANALYZE_SPEED * getUpgradeMultiply(UpgradeTypes.Speed));}
    public int getEnergyDrainPerTick() {return getEnergyDrainMax() / getSpeed();}
    public int getEnergyDrainMax() {return (int)Math.round(ENERGY_DRAIN_PER_ITEM * getUpgradeMultiply(UpgradeTypes.PowerUsage));}
    @Override
    public  boolean getServerActive() {return isAnalyzing();}
    @Override
    public String getSound() {return "analyzer";}
    @Override
    public boolean hasSound() {return true;}
    @Override
    public float soundVolume() { return 0.3f;}

    @Override
    public NBTTagCompound getFilter() {
        return componentMatterNetworkConfigs.getFilter();
    }
    public float getProgress(){return (float)analyzeTime / (float)getSpeed();}
    //endregion
}
