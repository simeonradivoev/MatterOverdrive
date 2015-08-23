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

package matteroverdrive.matter_network.components;

import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkFilter;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.IMachineComponent;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkPacketQueue;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.Level;

import java.util.EnumSet;

/**
 * Created by Simeon on 7/15/2015.
 */
public abstract class MatterNetworkComponentClient<T extends MOTileEntityMachine & IMatterNetworkConnection> implements IMatterNetworkClient, IMachineComponent<T>
{
    protected MatterNetworkPacketQueue<MatterNetworkPacket> packetQueue;
    protected T rootClient;

    public MatterNetworkComponentClient(T rootClient)
    {
        this.rootClient = rootClient;
        packetQueue = new MatterNetworkPacketQueue(rootClient);
    }

    public boolean executeBasicRequestPackets(MatterNetworkRequestPacket packet)
    {
        IMatterNetworkConnection sender = packet.getSender(getWorldObj());

        if (packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION || packet.getRequestType() == Reference.PACKET_REQUEST_CONNECTION) {

            if (packet.getRequest() instanceof Class)
            {
                if (((Class)packet.getRequest()).isInstance(rootClient))
                {
                    MatterNetworkHelper.respondToRequest(getWorldObj(),rootClient,packet,Reference.PACKET_RESPONCE_VALID,null);
                    return true;
                }
            } else {
                MatterNetworkHelper.respondToRequest(getWorldObj(),rootClient,packet,Reference.PACKET_RESPONCE_VALID,null);
                return true;
            }
        }
        return false;
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from)
    {
        if (canPreform(packet) && packet.isValid(getWorldObj()))
        {
            getPacketQueue(0).queue(packet);
            packet.tickAlive(getWorldObj(), true);
            packet.onAddedToQueue(getWorldObj(),getPacketQueue(0),0);
        }
    }

    protected void manageTopPacket()
    {
        for (int i = 0;i < getPacketQueueCount();i++)
        {
            if (getPacketQueue(i).peek() != null)
            {
                try {
                    executePacket(getPacketQueue(i).peek());
                }catch (Exception e)
                {
                    MatterOverdrive.log.log(Level.ERROR,e,"There was a problem while executing packet %s from queue %s",getPacketQueue(i).peek(),i);
                }finally
                {
                    getPacketQueue(i).dequeue();
                    getPacketQueue(i).tickAllAlive(getWorldObj(),true);
                }
            }
        }
    }

    protected abstract void executePacket(MatterNetworkPacket packet);

    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        if (phase == TickEvent.Phase.END)
        {
            manageTopPacket();
        }
        return 0;
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        if (packet.getFilter() != null)
        {
            NBTTagList connectionsList = packet.getFilter().getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_COMPOUND);
            for (int i = 0;i < connectionsList.tagCount();i++)
            {
                BlockPosition filterPos = new BlockPosition(connectionsList.getCompoundTagAt(i));
                if (filterPos.equals(getPosition()))
                {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    //region Getters and Setters
    @Override
    public BlockPosition getPosition()
    {
        return rootClient.getPosition();
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        return rootClient.canConnectFromSide(side);
    }

    @Override
    public MatterNetworkPacketQueue<MatterNetworkPacket> getPacketQueue(int queueID)
    {
        return packetQueue;
    }

    public World getWorldObj()
    {
        return rootClient.getWorldObj();
    }

    @Override
    public int getPacketQueueCount()
    {
        return 1;
    }
    //endregion

    //Machine Component
    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        if (categories.contains(MachineNBTCategory.DATA))
        {
            packetQueue.readFromNBT(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        if (categories.contains(MachineNBTCategory.DATA))
        {
            packetQueue.writeToNBT(nbt);
        }
    }

    @Override
    public void registerSlots(Inventory inventory) {

    }

    @Override
    public void update(T machine) {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onActiveChange(T machine) {

    }

    @Override
    public void onAwake(T machine, Side side) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving, T machine) {

    }
    //endregion
}
