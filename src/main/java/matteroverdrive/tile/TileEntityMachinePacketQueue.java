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

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter_network.IMatterNetworkClient;
import matteroverdrive.api.matter_network.IMatterNetworkComponent;
import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.data.transport.MatterNetwork;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.matter_network.components.MatterNetworkComponentQueue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/30/2015.
 */
public abstract class TileEntityMachinePacketQueue extends MOTileEntityMachine implements IMatterNetworkClient,IMatterNetworkConnection
{
    public static int BROADCAST_DELAY = 2;
    public static int TASK_QUEUE_SIZE = 16;
    protected MatterNetworkComponentQueue networkComponent;
    @SideOnly(Side.CLIENT)
    public int flashTime;

    public TileEntityMachinePacketQueue(int upgradeCount)
    {
        super(upgradeCount);
    }

    protected void registerComponents()
    {
        super.registerComponents();
        networkComponent = new MatterNetworkComponentQueue(this);
        addComponent(networkComponent);
    }

    @Override
    public void update()
    {
        super.update();
        if (worldObj.isRemote)
        {
            if (flashTime > 0)
            {
                flashTime--;
            }
        }
    }

    @Override
    public boolean canConnectFromSide(IBlockState blockState, EnumFacing side)
    {
        return true;
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
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type.equals(UpgradeTypes.Speed);
    }


    //region Matter Network
    /*@Override
    public int onNetworkTick(World world,TickEvent.Phase phase)
    {
        return networkComponent.onNetworkTick(world, phase);
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        return networkComponent.canPreform(packet);
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet)
    {
        networkComponent.queuePacket(packet);
    }

    @Override
    public MatterNetworkPacketQueue getPacketQueue(int queueID) {
        return networkComponent.getPacketQueue(queueID);
    }*/

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
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    protected void onAwake(Side side)
    {

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
    public float soundVolume() { return 0;}

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 1024.0D;
    }

    @Override
    public IMatterNetworkComponent getMatterNetworkComponent()
    {
        return networkComponent;
    }

/*    @Override
    public int getPacketQueueCount() {
        return networkComponent.getPacketQueueCount();
    }*/
    //endregion
}
