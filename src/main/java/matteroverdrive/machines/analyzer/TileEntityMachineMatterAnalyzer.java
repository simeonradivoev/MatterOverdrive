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
import matteroverdrive.api.matter_network.IMatterNetworkClient;
import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.network.*;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.data.transport.MatterNetwork;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.matter_network.components.MatterNetworkComponentClient;
import matteroverdrive.matter_network.components.TaskQueueComponent;
import net.minecraft.block.state.IBlockState;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.data.inventory.DatabaseSlot;
import matteroverdrive.data.inventory.MatterSlot;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.items.MatterScanner;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.components.ComponentMatterNetworkConfigs;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import matteroverdrive.tile.MOTileEntityMachineEnergy;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.EnumSet;

/**
 * Created by Simeon on 3/16/2015.
 */
public class TileEntityMachineMatterAnalyzer extends MOTileEntityMachineEnergy implements ISidedInventory,IMatterNetworkClient,IMatterNetworkConnection, IMatterNetworkDispatcher
{
    private static final EnumSet<UpgradeTypes> upgradeTypes = EnumSet.of(UpgradeTypes.PowerUsage,UpgradeTypes.PowerStorage,UpgradeTypes.Speed,UpgradeTypes.PowerStorage);
    public static final int ENERGY_STORAGE = 512000;
    public static final int ENERGY_TRANSFER = 1024;

    public int input_slot = 0;
    private ComponentMatterNetworkAnalyzer networkComponent;
    private ComponentMatterNetworkConfigs componentMatterNetworkConfigs;
    private ComponentTaskProcessingAnalyzer taskProcessingComponent;

    public TileEntityMachineMatterAnalyzer()
    {
        super(4);
        this.energyStorage.setCapacity(ENERGY_STORAGE);
        this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
        this.energyStorage.setMaxReceive(ENERGY_TRANSFER);
        playerSlotsHotbar = true;
        playerSlotsMain = true;
    }

    @Override
    public void RegisterSlots(Inventory inventory)
    {
        input_slot = inventory.AddSlot(new MatterSlot(true));
        super.RegisterSlots(inventory);
    }

    @Override
    protected void registerComponents()
    {
        super.registerComponents();
        componentMatterNetworkConfigs = new ComponentMatterNetworkConfigs(this);
        networkComponent = new ComponentMatterNetworkAnalyzer(this);
        taskProcessingComponent = new ComponentTaskProcessingAnalyzer("Tasks",this,1,0);
        addComponent(componentMatterNetworkConfigs);
        addComponent(networkComponent);
        addComponent(taskProcessingComponent);
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return upgradeTypes.contains(type);
    }

    //region Inventory Methods
    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing side)
    {
        return true;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if(side == EnumFacing.UP)
        {
            return new int[]{input_slot};
        }
        else
        {
            return new int[]{input_slot};
        }
    }

    //endregion

    //region Matter Network Functions

    @Override
    public boolean canConnectFromSide(IBlockState blockState, EnumFacing side)
    {
        EnumFacing facing = blockState.getValue(MOBlock.PROPERTY_DIRECTION);
        return facing.getOpposite() == side;
    }

    @Override
    public boolean establishConnectionFromSide(IBlockState blockState, EnumFacing side)
    {
        return canConnectFromSide(blockState, side);
    }

    @Override
    public void breakConnection(IBlockState blockState, EnumFacing side)
    {

    }

    @Override
    public MatterNetwork getNetwork()
    {
        return networkComponent.getNetwork();
    }

    @Override
    public void setNetwork(MatterNetwork network)
    {
        networkComponent.setNetwork(network);
    }

    @Override
    public boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction)
    {
        return networkComponent.canConnectToNetworkNode(blockState, toNode, direction);
    }

    //endregion

    //region Events
    @Override
    protected void onMachineEvent(MachineEvent event)
    {
        if (event instanceof MachineEvent.ActiveChange)
        {
            forceSync();
        }
    }
    //endregion

    //region Getters and Setters
    @Override
    public  boolean getServerActive() {
        return taskProcessingComponent.isAnalyzing();
    }
    @Override
    public String getSound() {return "analyzer";}
    @Override
    public boolean hasSound() {return true;}
    @Override
    public float soundVolume() { return 0.3f;}

    public float getProgress(){return taskProcessingComponent.getProgress();}

    public int getEnergyDrainPerTick() {return taskProcessingComponent.getEnergyDrainPerTick();}
    public int getEnergyDrainMax() {return taskProcessingComponent.getEnergyDrainMax();}

    @Override
    public MatterNetworkComponentClient getMatterNetworkComponent()
    {
        return networkComponent;
    }

    @Override
    public MatterNetworkTaskQueue getTaskQueue(int queueID)
    {
        return taskProcessingComponent.getTaskQueue();
    }

    @Override
    public int getTaskQueueCount()
    {
        return 1;
    }
    //endregion
}
