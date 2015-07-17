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

import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkPacketQueue;
import matteroverdrive.matter_network.MatterNetworkQueue;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.components.MatterNetworkComponentQueue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/30/2015.
 */
public abstract class TileEntityMachinePacketQueue extends MOTileEntityMachine implements IMatterNetworkClient
{
    public static int BROADCAST_DELAY = 2;
    public static int TASK_QUEUE_SIZE = 16;
    private MatterNetworkPacketQueue<MatterNetworkPacket> packetQueue;
    private MatterNetworkComponentQueue networkComponent;
    private BlockPosition[] connections;

    public TileEntityMachinePacketQueue(int upgradeCount)
    {
        super(upgradeCount);
        packetQueue = new MatterNetworkPacketQueue(this,TASK_QUEUE_SIZE);
        connections = new BlockPosition[6];
        redstoneMode = Reference.MODE_REDSTONE_NONE;
        networkComponent = new MatterNetworkComponentQueue(this);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        return true;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type.equals(UpgradeTypes.Speed);
    }

    //region NBT
    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        packetQueue.readFromNBT(nbt);
        for (int i = 0;i < connections.length;i++)
        {
            if (nbt.hasKey("Connection" + i)) {
                BlockPosition position = new BlockPosition(nbt.getCompoundTag("Connection" + i));
                if (worldObj != null) {
                    TileEntity tileEntity = position.getTileEntity(worldObj);
                    if (tileEntity != null && tileEntity instanceof IMatterNetworkConnection) {
                        connections[i] = ((IMatterNetworkConnection) tileEntity).getPosition();
                    }
                }
            }
        }
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        packetQueue.writeToNBT(nbt);
        for (int i = 0;i < connections.length;i++)
        {
            if (getConnection(i) != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                connections[i].writeToNBT(tagCompound);
                nbt.setTag("Connection" + i,tagCompound);
            }
        }
    }
    //endregion

    //region Matter Network
    @Override
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
    public void queuePacket(MatterNetworkPacket packet,ForgeDirection from)
    {
        networkComponent.queuePacket(packet, from);
    }

    @Override
    public BlockPosition getPosition()
    {
        return new BlockPosition(xCoord,yCoord,zCoord);
    }

    public MatterNetworkPacketQueue<MatterNetworkPacket> getPacketQueue()
    {
        return packetQueue;
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
    public boolean isActive()
    {
        return packetQueue.size() > 0;
    }

    @Override
    public float soundVolume() { return 0;}

    public BlockPosition getConnection(int id)
    {
        if (connections[id] != null)
        {
            if (connections[id].getTileEntity(worldObj) != null && connections[id].getTileEntity(worldObj) instanceof IMatterNetworkConnection)
                return connections[id];
            else {
                connections[id] = null;
                return null;
            }
        }
        return connections[id];
    }

    public void setConnection(int id,BlockPosition position)
    {
        if (connections[id] != null)
        {
            connections[id] = position;
        }
    }

    public BlockPosition[] getConnections()
    {
        return connections;
    }
    //endregion
}
