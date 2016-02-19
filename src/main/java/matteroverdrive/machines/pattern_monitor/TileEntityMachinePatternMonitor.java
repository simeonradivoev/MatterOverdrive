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

package matteroverdrive.machines.pattern_monitor;

import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.matter_network.IMatterNetworkClient;
import matteroverdrive.api.matter_network.IMatterNetworkComponent;
import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.container.matter_network.IMatterDatabaseMonitor;
import matteroverdrive.data.transport.MatterNetwork;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.components.TaskQueueComponent;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.components.ComponentMatterNetworkConfigs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.*;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TileEntityMachinePatternMonitor extends MOTileEntityMachine implements IMatterNetworkClient,IMatterDatabaseMonitor,IMatterNetworkDispatcher,IMatterNetworkConnection
{
    private ComponentMatterNetworkPatternMonitor networkComponent;
    private ComponentMatterNetworkConfigs componentMatterNetworkConfigs;
    private ComponentTaskProcessingPatternMonitor taskProcessingComponent;

    public TileEntityMachinePatternMonitor()
    {
        super(4);
        playerSlotsHotbar = true;
    }

    //region Machine Functions
    @Override
    protected void registerComponents() {
        super.registerComponents();
        networkComponent = new ComponentMatterNetworkPatternMonitor(this);
        componentMatterNetworkConfigs = new ComponentMatterNetworkConfigs(this);
        taskProcessingComponent = new ComponentTaskProcessingPatternMonitor("Replication Tasks",this,8,0);
        addComponent(networkComponent);
        addComponent(componentMatterNetworkConfigs);
        addComponent(taskProcessingComponent);
    }

    @Override
    protected void onMachineEvent(MachineEvent event)
    {

    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }
    //endregion

    //region NBT
    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {
        super.writeCustomNBT(nbt, categories, toDisk);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(nbt, categories);
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
        return networkComponent.establishConnectionFromSide(blockState, side);
    }

    @Override
    public void breakConnection(IBlockState blockState, EnumFacing side)
    {
        networkComponent.breakConnection(blockState, side);
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

    //region Matter Database Watcher
    @Override
    public List<IMatterDatabase> getConnectedDatabases()
    {
        List<IMatterNetworkClient> clients = getNetwork().getClients();
        List<IMatterDatabase> databases = new ArrayList<>();
        for (IMatterNetworkClient client : clients)
        {
            if (client instanceof IMatterDatabase)
            {
                databases.add((IMatterDatabase)client);
            }
        }
        return databases;
    }
    //endregion

    //region Getters and Setters
    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean getServerActive()
    {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }
    @Override
    public IMatterNetworkComponent getMatterNetworkComponent()
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
